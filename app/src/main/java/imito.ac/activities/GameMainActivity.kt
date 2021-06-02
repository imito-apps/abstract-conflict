package imito.ac.activities

import android.os.*
import android.util.*
import android.view.*
import android.widget.*
import androidx.core.content.*
import androidx.recyclerview.widget.*
import imito.core.*
import imito.core.errors.*
import imito.core.views.*
import imito.core.views.dialogs.*
import imito.core.views.lists.*
import imito.ac.*
import imito.ac.activities.adapters.*
import imito.ac.activities.adapters.user.*
import imito.ac.activities.dialogs.*
import imito.ac.activities.dialogs.card.*
import imito.ac.game.*
import imito.ac.models.*
import imito.ac.models.cards.*
import imito.ac.notifiers.*

class GameMainActivity : PortraitActivity(R.layout.activity_game_main) {
    companion object {
        const val Name = "GameMainActivity"
        var openedCard: CardModel? = null
    }

    private val game = GameProvider.Instance
    private lateinit var notifier: GameNotifier
    private val gameState: GameStateModel? get() = game.stateModel

    private val adapterOfDiscarded = CardAdapter {
        if (!it.isShown) return@CardAdapter

        openDialog {
            CardDialog(this, it, ::onDialogClosed)
        }
    }
    private val adapterOfHand = CardAdapter {
        openDialog {
            openedCard = it
            it.playSelf(this, game, ::onDialogClosed)
        }
    }
    private val adapterOfPlayers = PlayerAdapter { }
    private lateinit var handLabel: TextView
    private lateinit var winPointCountLabel: TextView
    private lateinit var exitButton: ImageView
    private lateinit var stockCountLabel: TextView

    private var markColor: Int = 0
    private var primaryColor: Int = 0

    override fun createSelf(savedInstanceState: Bundle?) {
        setLateInitProperties()
        setStatusBar()
        setExits()
        setLists()

        Thread.sleep(100)

        game.stateChangedListeners[Name] = ::handleGameState
        if (gameState != null) handleGameState()

        if (openedCard == null) return

        openDialog {
            openedCard!!.playSelf(this, game, ::onDialogClosed)
        }
    }

    override fun onDestroy() {
        game.stateChangedListeners.remove(Name)
        super.onDestroy()
    }

    override fun onBackPressed() {}

    private fun setExits() {
        game.abort = ::finish
        exitButton.setOnClickListener {
            YesNoDialog.show(this, R.string.dialog_abort_server, {
                game.abortSelf()
                finish()
            })
        }
    }

    private fun setStatusBar() {
        winPointCountLabel = findViewById(R.id.label_win_points)
        stockCountLabel = findViewById(R.id.button_stock_count)
        exitButton = findViewById(R.id.button_abort_game)

        addToastTo(winPointCountLabel, R.string.description_win_points)
        addToastTo(stockCountLabel, R.string.description_stock_count)
    }

    private fun addToastTo(textView: TextView, resourceId: Int) {
        textView.setOnClickListener {
            SimpleToast.show(textView, resourceId)
        }
    }

    private fun setLateInitProperties() {
        notifier = GameNotifier(resources)
        markColor = getMarkColor()
        primaryColor = getPrimaryColor()
    }

    private fun ensureStartInfo() {
        if (game.getValueOrFalseAndSetTrue(GameConfig.Keys.GuideGameMain)) return

        val format = resources.getString(R.string.guide_game_main_activity_start)
        val textEndId = if (game.isPlayerTheCurrentOne) R.string.guide_game_main_activity_play_and_wait
        else R.string.guide_game_main_activity_wait_for_turn

        val textEnd = resources.getString(textEndId)
        val message = String.format(format, textEnd)
        OkDialog.show(this, message)
    }

    private fun ensureTwoPlayersInfo() {
        if (game.players.size > 2
            || game.getValueOrFalseAndSetTrue(GameConfig.Keys.TwoPlayersDiscard)
        ) return

        val message = resources.getString(R.string.game_main_activity_two_players_discard)
        OkDialog.show(this, message)
    }

    private fun setLists() {
        handLabel = findViewById(R.id.label_hand)

        setRecyclerView(R.id.recycler_discarded, adapterOfDiscarded)
        setRecyclerView(R.id.recycler_hand, adapterOfHand)
        setRecyclerView(R.id.recycler_player, adapterOfPlayers)
    }

    private fun <TItem, TLayout : ViewGroup, THolder : RecyclerViewHolder<TLayout>> setRecyclerView(
        id: Int,
        adapter: RecyclerAdapter<TItem, TLayout, THolder>,
    ) {
        val recyclerView = findViewById<RecyclerView>(id)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter = adapter
    }

    private fun handleGameState() {
        if (game.playerCards.isEmpty) return

        runOnUiThread {
            ExceptionCatcher.tryRun<Exception>({
                handle(gameState!!)
            }, "handleGameState")
        }
    }

    private fun handle(gameState: GameStateModel) {
        ensureStartInfo()
        ensureTwoPlayersInfo()
        updateStatusBar(gameState)
        updateLists(gameState)
        updateHandLabelColor()

        notifier.addFrom(game)
        showNotification()
        if (!gameState.isRoundEnd) return

        addRoundWinNotification(gameState)
        if (addGameWinNotification(gameState)) game.end()
    }

    private fun updateLists(gameState: GameStateModel) {
        adapterOfDiscarded.replaceAll(gameState.discardPile)
        adapterOfHand.replaceAll(game.playerCards)
        adapterOfPlayers.replaceAll(gameState.players)
    }

    private fun updateStatusBar(gameState: GameStateModel) {
        winPointCountLabel.text = "${gameState.winPointCount}"
        stockCountLabel.text = "${gameState.stockCount}"
    }

    private fun addGameWinNotification(gameState: GameStateModel): Boolean {
        val gameWinPlayers = gameState.winners
        if (gameWinPlayers.none()) return false

        val message = resources.getString(R.string.message_game_won_by)
        val notification = gameWinPlayers.joinToString(", ", "\n$message ", ".") { it.name }
        addNotification(notification)
        return true
    }

    private fun addRoundWinNotification(gameState: GameStateModel) {
        val standingPlayers = gameState.standingPlayers
        var notification = getStandingPlayersNotification(standingPlayers)
        val expansionPlayer = standingPlayers.firstOrNull { it.wonByExpansion }
        if (expansionPlayer != null) {
            val message = resources.getString(R.string.message_get_point_by_expansion)
            notification += "\n$message ${expansionPlayer.name}."
        }
        addNotification(notification)
        showNotification()
    }

    private fun getStandingPlayersNotification(players: PlayerModels): String {
        val lastStandingPlayer = players.firstOrNull { it.wonByElimination }
        return if (lastStandingPlayer == null) {
            val message = resources.getString(R.string.message_get_point_by_points)
            players.filter { it.wonByPoints }
                .joinToString(", ", "$message ", ".") { it.name }
        } else {
            val message = resources.getString(R.string.message_get_point_by_elimination)
            "$message ${lastStandingPlayer.name}."
        }
    }

    private fun addNotification(value: String) = notifier.addMessage(value)

    private fun showNotification() = notifier.showMessage(this, game)

    private var hasOpenedDialog = false
    private fun openDialog(open: () -> Unit) {
        if (hasOpenedDialog) return

        hasOpenedDialog = true
        open()
    }

    private fun onDialogClosed() {
        hasOpenedDialog = false
        openedCard = null
    }

    private fun updateHandLabelColor() {
        val handLabelColor = if (game.isPlayerTheCurrentOne)
            markColor
        else primaryColor
        handLabel.setTextColor(handLabelColor)
    }

    private fun getMarkColor() = ContextCompat.getColor(this, R.color.green)

    private fun getPrimaryColor(): Int {
        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
        return typedValue.data
    }
}
