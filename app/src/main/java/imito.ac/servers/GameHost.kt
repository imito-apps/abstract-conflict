package imito.ac.servers

import androidx.appcompat.app.*
import imito.core.*
import imito.core.entities.*
import imito.core.errors.*
import imito.core.nsd.*
import imito.ac.*
import imito.ac.entities.*
import imito.ac.entities.cards.*
import imito.ac.entities.game.*
import imito.ac.entities.game.notifications.*
import imito.ac.entities.player.*
import imito.ac.game.*
import imito.ac.serializers.*
import java.io.*
import java.net.*
import java.util.*
import kotlin.concurrent.thread

class GameHost(
    activity: AppCompatActivity,
    gameName: String,
    private val changeOthers: (players: List<NetUser>) -> Unit,
    private val handleGameState: (GameState) -> Unit,
    private val abortByClient: (name: String) -> Unit,
) : GameParticipant(gameName) {
    private val nsdRegisterer = NsdRegisterer(activity, gameName)
    private val isSummoning = BoolObject(false)
    private val otherPlayers = mutableMapOf<UUID, NetPlayer>()
    private var hostPlayer: NetUser? = null
    private var playerPointer = PlayerPointer(listOf(Const.Guid.Nil))
    private val game get() = GameProvider.Instance
    val hasAnyClient get() = otherPlayers.isNotEmpty()

    fun getRegisteredName(): String = nsdRegisterer.getServiceName()

    fun startSummoning(
        player: NetUser,
    ) {
        synchronized(isSummoning) {
            if (isSummoning.value) return

            hostPlayer = player
            isSummoning.value = true
            server.start({ _, port ->
                nsdRegisterer.register(port, player)
            }, { text, address ->
                val payload = text.substring(1)
                when (text[0]) {
                    Message.Join -> {
                        handleJoin(payload, address)
                    }
                    Message.Leave -> {
                        val playerId = UUID.fromString(payload)
                        synchronized(otherPlayers) {
                            otherPlayers.remove(playerId)
                        }
                        notifyPlayersAboutThemselves {}
                    }
                    Message.Decision -> {
                        val netPlayer = getNetPlayer(address) ?: return@start

                        val decision = PlayerDecision.deserialize(payload)
                        applyDecision(netPlayer.value.id, decision)
                    }
                    Message.AbortByClient -> {
                        val netPlayer = getNetPlayer(address) ?: return@start

                        abortByClient(netPlayer.value.name)
                    }
                }
            })
        }
    }

    private fun getNetPlayer(address: InetAddress) = otherPlayers.values
        .firstOrNull { it.address == address }

    private fun handleJoin(payload: String, address: InetAddress) {
        val playerAsText = payload.substring(PadLength)
        val newUser = userSerializer.deserialize(playerAsText, address) ?: return
        val newPlayer = NetPlayer(newUser, playerAsText)
        fun respond(body: Response.Join) {
            val message = "${Message.ResponseToJoin}${body.value}"
            sendMessageToClient(newPlayer, message)
        }
        if (otherPlayers.size >= 5) {
            return respond(Response.Join.TooManyPlayers)
        }
        val version = getVersion(payload)
        if (version > AppConst.VersionCode) {
            return respond(Response.Join.VersionTooNew)
        }
        if (version < AppConst.FirstCompatibleVersion) {
            return respond(Response.Join.VersionTooOld)
        }
        if (newUser.id == Const.Guid.Nil) return

        synchronized(otherPlayers) {
            otherPlayers[newUser.id] = newPlayer
        }
        respond(Response.Join.Ok)
        notifyPlayersAboutThemselves {}
    }

    fun abortGame() {
        notifyPlayersAboutAbortion {
            cleanAfterGame()
        }
    }

    fun startGame() {
        synchronized(isSummoning) {
            if (!isSummoning.value) return

            stopSummoning()
            notifyPlayersAboutThemselves {
                notifyPlayersAboutGameStart {
                    val netPlayers = mutableListOf<NetUser>()
                    otherPlayers.values.mapTo(netPlayers) { it.value }
                    netPlayers.add(hostPlayer!!)
                    val players = netPlayers.map {
                        val marks = PlayerMarks(false, false, false, false, false, false)
                        Player(it.id, it.name, Cards(listOf()), 0, marks)
                    }
                    playerPointer = PlayerPointer(players.map { it.id })
                    val initialState = GameState.create(playerPointer.currentId, players, GameNotification(), true)
                    notifyPlayersAboutGameState(initialState)
                }
            }
        }
    }

    private val decisionLock = Object()

    fun applyDecision(playerId: UUID, decision: PlayerDecision) {
        synchronized(decisionLock) {
            var gameState = game.stateEntity!!
            if (playerId != gameState.currentPlayerId) return

            if (gameState.isRoundEnd) gameState = gameState.withClearedPlayers()

            gameState = gameState.applyDecision(decision)
            gameState = when {
                gameState.standingPlayers.size == 1 -> gameState.winLastStandingPlayer()
                gameState.stockPile.isEmpty -> gameState.winPlayersWithBestCard()
                else -> {
                    do {
                        playerPointer = playerPointer.nextId()
                    } while (!gameState.standingPlayers.any { it.id == playerPointer.currentId })
                    gameState.withCurrentPlayer(playerPointer.currentId)
                        .changeMarksOfCurrent {
                            it.withDefence(false)
                        }
                        .dealCardToCurrent()
                }
            }
            if (gameState.isRoundEnd) {
                playerPointer = playerPointer.withCurrentId(gameState.currentPlayerId)
                gameState = gameState.winExpansionPlayer()
            }
            notifyPlayersAboutGameState(gameState.incrementOrderNumber())
        }
    }

    private fun notifyPlayersAboutGameState(state: GameState) {
        val message = "${Message.GameState}${state.serialize()}"
        notifyPlayersAbout(message) {}
        handleGameState(state)
    }

    fun abortSummoning() {
        synchronized(isSummoning) {
            if (!isSummoning.value) return

            stopSummoning()
            notifyPlayersAboutAbortion {
                cleanAfterGame()
            }
        }
    }

    private fun stopSummoning() {
        isSummoning.value = false
        nsdRegisterer.unregister()
    }

    fun cleanAfterGame() {
        hostPlayer = null
        server.close()
        otherPlayers.clear()
    }

    private fun notifyPlayersAboutThemselves(then: () -> Unit) {
        if (hostPlayer == null) return

        var playersAsText = serializePlayer(hostPlayer!!)
        for ((_, netPlayer) in otherPlayers) {
            playersAsText += Message.Separator + netPlayer.valueAsText
        }
        val message = "${Message.Others}$playersAsText"
        notifyPlayersAbout(message, then)
        val players = otherPlayers.values
            .map { it.value }
        changeOthers(players)
    }

    private fun notifyPlayersAboutAbortion(then: () -> Unit) {
        val message = "${Message.Abort}${hostPlayer?.name}"
        notifyPlayersAbout(message, then)
    }

    private fun notifyPlayersAboutGameStart(then: () -> Unit) {
        notifyPlayersAbout(Message.Start, then)
    }

    private fun notifyPlayersAbout(message: Char, then: () -> Unit) {
        notifyPlayersAbout(message.toString(), then)
    }

    private fun notifyPlayersAbout(
        message: String,
        then: () -> Unit,
    ) {
        thread {
            for ((_, netPlayer) in otherPlayers) {
                sendMessageToClient(netPlayer, message)
            }
            then()
        }
    }

    private fun sendMessageToClient(netPlayer: NetPlayer, message: String) {
        ExceptionCatcher.tryRun<ConnectException>({
            Client(netPlayer.address, netPlayer.port)
                .send(message)
        }, "GameHost.sendMessageToClient ${message.first()}")
    }

    private fun getVersion(payload: String): Int {
        return payload
            .substring(0, PadLength)
            .trim()
            .toInt()
    }

    private class NetPlayer(
        val value: NetUser,
        val valueAsText: String,
    ) {
        val address: InetAddress get() = value.address
        val port: Int get() = value.port
    }

    private class BoolObject(
        var value: Boolean,
    )
}
