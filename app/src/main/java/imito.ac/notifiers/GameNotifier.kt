package imito.ac.notifiers

import imito.ac.*
import android.content.res.*
import androidx.appcompat.app.*
import imito.ac.entities.game.notifications.*
import imito.ac.game.*
import imito.ac.models.cards.*
import java.lang.*
import java.util.*

class GameNotifier(
    private val resources: Resources,
) {
    private val mainMessage = resources.getString(R.string.message_main)
    private val notifier = AppNotifier()
    private var previousPlayerId: UUID? = null

    fun addMessage(value: String) {
        if (value.isNotBlank()) notifier.addMessage(value)
    }

    fun showMessage(activity: AppCompatActivity, game: Game): Unit = notifier.showMessage(activity) {
        if (game.isEnded && notifier.isEmpty) activity.finish()
        else showMessage(activity, game)
    }

    fun addFrom(game: Game) {
        val gameState = game.stateModel!!
        val notification = gameState.notification
        val notificationParts = if (game.playerId == previousPlayerId)
            notification.toPrevious
        else notification.toOthers
        val message = getMessage(notificationParts, game)

        addMessage(message)
        previousPlayerId = gameState.currentPlayerId
    }

    private fun getMessage(notificationParts: NotificationParts, game: Game): String {
        val stringBuilder = StringBuilder()
        notificationParts.forEach {
            val text = resources.getString(it.textId)
            val args = getArgs(it, game)
            val message = String.format(text, args.first, args.second)
            stringBuilder.append("$message ")
        }
        return stringBuilder.toString()
    }

    private fun getArgs(notificationPart: NotificationPart, game: Game): Pair<String, String> {
        return when (notificationPart.textId) {
            R.string.message_main -> {
                val cardName = getCardName(notificationPart)
                Pair(notificationPart.arg1, cardName)
            }
            R.string.message_player_targeted -> {
                Pair(notificationPart.arg1, "")
            }
            R.string.message_card_targeted -> {
                val cardsForPoint = game
                    .cardsForPoint[notificationPart.arg1.toInt()]
                    ?.joinToString(", ") { it.name }
                    ?: ""
                Pair(notificationPart.arg1, cardsForPoint)
            }
            R.string.message_targeted_stays -> {
                Pair("", "")
            }
            R.string.message_targeted_leaves -> {
                Pair("", "")
            }
            R.string.message_draw -> {
                Pair("", "")
            }
            R.string.message_lower_leaves -> {
                Pair(notificationPart.arg1, "")
            }
            R.string.message_card_revealed -> {
                val cardName = getCardName(notificationPart)
                Pair(notificationPart.arg1, cardName)
            }
            R.string.message_domination_stays -> {
                Pair("", "")
            }
            R.string.message_domination_leaves -> {
                Pair("", "")
            }
            else -> Pair("", "")
        }
    }

    private fun getCardName(notificationPart: NotificationPart): String {
        val cardId = notificationPart.arg2.toInt()
        return CardModelFactory.create(cardId, resources, true).name
    }
}
