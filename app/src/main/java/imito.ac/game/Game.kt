package imito.ac.game

import android.view.*
import androidx.appcompat.app.*
import imito.core.entities.*
import imito.core.nsd.*
import imito.core.views.*
import imito.ac.*
import imito.ac.entities.game.*
import imito.ac.entities.player.*
import imito.ac.models.*
import imito.ac.models.cards.*
import imito.ac.servers.*
import java.net.*
import java.util.*

class Game(
    val activity: AppCompatActivity,
    val view: View,
    val onHostFound: (UserBase) -> Unit,
    val onHostLost: (UUID) -> Unit,
) {
    private val config = GameConfig(activity)
    private var player = NetUser(config.getUserId(), InetAddress.getByName(""), "", 0)
    val discoveredHosts = mutableMapOf<UUID, DiscoveredService>()

    private val gameHost = GameHost(activity, AppConst.GameName, ::onOthersChange, ::onHandleSelfState, ::onAbortSelf)
    private val gameClient = GameClient(
        AppConst.GameName,
        ::setPlayerAddress,
        ::onOthersChange,
        ::onStartSelf,
        ::onHandleSelfState,
        ::onAbortSelf,
    )
    private val gameDiscoverer = NsdDiscoverer(activity, AppConst.GameName, gameHost::getRegisteredName,
        {
            discoveredHosts[it.id] = it
            onHostFound(it.toUser())
        }, {
            discoveredHosts.remove(it)
            onHostLost(it)
        })
    private var setDiscoveredHost: DiscoveredService? = null
    private var changeOthers: (List<NetUser>) -> Unit = {}
    private var start: () -> Unit = {}
    var abort: () -> Unit = {}
    val stateChangedListeners = mutableMapOf<String, () -> Unit>()
    var stateEntity: GameState? = null
    var stateModel: GameStateModel? = null

    val isEnded get() = stateEntity == null
    val isPlayerTheHost get() = gameHost.hasAnyClient
    val isPlayerTheCurrentOne get() = playerId == stateEntity!!.currentPlayerId
    val playerId get() = player.id
    val playerName get() = player.name
    val playerCards get() = stateModel!!.players.first { it.id == playerId }.cards
    val otherPlayers get() = stateModel!!.players.filter { it.id != playerId }
    val players get() = stateModel!!.players
    val cardsForPoint get() = CardModelFactory.getCollection(stateEntity!!.isBasicDeck, activity.resources).valuesForPoint

    init {
        AppConst.FirstCompatibleVersion
        val playerName = config.getUserName()
        setPlayerName(playerName)
    }

    fun setDiscoveredHost(hostId: UUID) {
        setDiscoveredHost = discoveredHosts[hostId] ?: return
    }

    fun joinHost(
        changeOthers: (players: List<NetUser>) -> Unit,
        onStart: () -> Unit,
        onResponse: (GameParticipant.Response.Join) -> Unit,
    ) {
        this.changeOthers = changeOthers
        this.start = {
            discoveredHosts.remove(setDiscoveredHost!!.id)
            onStart()
        }
        gameClient.joinHost(setDiscoveredHost!!, player, onResponse)
    }

    fun leaveHost() {
        gameClient.leaveHost(playerId)
    }

    private fun setPlayerName(name: String) {
        player = player.withName(name)
    }

    private fun setPlayerAddress(address: InetAddress, port: Int) {
        player = player.withAddress(address, port)
    }

    private fun onOthersChange(players: List<NetUser>) {
        val others = players.filter { it.id != playerId }
        changeOthers(others)
    }

    private fun onStartSelf() {
        start()
    }

    private fun onAbortSelf(playerName: String) {
        removeState()
        abort()
        val format = activity.resources
            .getString(R.string.info_game_aborted)
        val message = String.format(format, playerName)
        activity.runOnUiThread {
            SimpleToast.show(activity, view, message)
        }
    }

    private fun onHandleSelfState(gameState: GameState) {
        if (stateEntity?.orderNumber ?: -1 >= gameState.orderNumber) return

        stateEntity = gameState
        stateModel = GameStateModel(gameState, activity.resources)
        stateChangedListeners.values.forEach { it.invoke() }
    }

    fun saveName(name: String): Boolean {
        val truncatedName = config.saveUserName(name) ?: return false

        setPlayerName(truncatedName)
        return true
    }

    fun getValueOrFalseAndSetTrue(key: String): Boolean {
        return config.getValueOrFalseAndSetTrue(key)
    }

    fun onAppStart() {
        gameDiscoverer.onAppStart()
    }

    fun onAppStop() {
        config.save()
        gameDiscoverer.onAppStop()
    }

    fun onAppDestroy() {
        gameHost.abortSummoning()
        abortSelf()
    }

    fun startSelf() {
        gameHost.startGame()
    }

    fun abortSelf() {
        if (isPlayerTheHost) {
            removeState()
            gameHost.abortGame()
        } else {
            gameClient.abortGame()
        }
    }

    fun end() {
        removeState()
        if (isPlayerTheHost) gameHost.cleanAfterGame()
    }

    fun abortSummoning() {
        gameHost.abortSummoning()
    }

    fun startSummoning(
        changeOthers: (players: List<NetUser>) -> Unit,
    ) {
        this.changeOthers = changeOthers
        gameHost.startSummoning(player)
    }

    fun makeDecision(decision: PlayerDecision) {
        if (!isPlayerTheCurrentOne) return

        if (isPlayerTheHost)
            gameHost.applyDecision(playerId, decision)
        else
            gameClient.makeDecision(decision)
    }

    private fun removeState() {
        stateEntity = null
        stateModel = null
    }
}
