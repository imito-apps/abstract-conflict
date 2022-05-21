package imito.ac.servers

import imito.core.entities.*
import imito.core.errors.*
import imito.ac.*
import imito.ac.entities.game.*
import imito.ac.entities.player.*
import imito.ac.serializers.*
import java.net.*
import java.util.*
import kotlin.concurrent.thread

class GameClient(
    gameName: String,
    onStarted: (InetAddress, port: Int) -> Unit,
    changeOthers: (List<NetUser>) -> Unit,
    startGame: () -> Unit,
    handleGameState: (GameState) -> Unit,
    abort: (name: String) -> Unit,
) : GameParticipant(gameName) {
    private var connectedHost: DiscoveredService? = null
    private var onResponseToJoin: (Response.Join) -> Unit = {}

    init {
        thread {
            server.start(onStarted) { text, wrongAddressToDo ->
                val payload = text.substring(1)
                when (text[0]) {
                    Message.Others -> {
                        val players = payload.split(Message.Separator)
                            .mapNotNull { deserializePlayer(it, wrongAddressToDo) }
                        changeOthers(players)
                    }
                    Message.Abort -> {
                        abort(payload)
                    }
                    Message.Start -> {
                        startGame()
                    }
                    Message.GameState -> {
                        handleGameState(GameState.deserialize(payload))
                    }
                    Message.ResponseToJoin -> {
                        ExceptionCatcher.tryRun<Exception>({
                            val index = payload.toInt() - 1
                            val response = Response.JoinValues[index]
                            onResponseToJoin(response)
                        }, "GameClient.ResponseToJoin")
                    }
                }
            }
        }
    }

    fun joinHost(
        host: DiscoveredService,
        player: NetUser,
        onResponse: (Response.Join) -> Unit,
    ) {
        connectedHost = host
        onResponseToJoin = onResponse
        val playerAsText = serializePlayer(player)
        val versionCode = AppConst.VersionCode
            .toString()
            .padStart(PadLength)
        val message = "${Message.Join}$versionCode$playerAsText"
        sendMessageToHost(message)
    }

    fun leaveHost(playerId: UUID) {
        if (connectedHost == null) return

        val message = "${Message.Leave}$playerId"
        sendMessageToHost(message, CommonErrorMessages.ForConnect)
    }

    fun makeDecision(decision: PlayerDecision) {
        val message = "${Message.Decision}${decision.serialize()}"
        sendMessageToHost(message)
    }

    fun abortGame() {
        sendMessageToHost("${Message.AbortByClient}", CommonErrorMessages.ForConnect)
    }

    private fun sendMessageToHost(
        message: String,
        allowedMessages: Array<String> = ExceptionCatcher.emptyAllowedMessages,
    ) {
        if (connectedHost == null) return

        thread {
            ExceptionCatcher.tryRun<ConnectException>({
                Client(connectedHost!!.address!!, connectedHost!!.port)
                    .send(message)
            }, allowedMessages, "GameClient.sendMessageToHost")
        }
    }
}
