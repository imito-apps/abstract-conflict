package imito.ac.activities

import android.app.*
import android.os.*
import imito.ac.entities.cards.sets.main.base.*
import imito.ac.game.*
import imito.ac.models.cards.*

class CardActivator(
    private val card: CardModel,
    private val game: Game,
) {
    fun watchState(
        activity: Activity,
        activate: () -> Unit,
    ) {
        var hasRoundEnded = game.stateModel!!.isRoundEnd
        game.stateChangedListeners[card.name] = {
            if (game.stateModel!!.isRoundEnd) hasRoundEnded = true

            if (game.isPlayerTheCurrentOne
                && !hasRoundEnded
                && game.playerCards.hasId(card.id)
                && (!game.stateModel!!.exploitationHasToBeUsed
                        || ExploitationBase.Ids.contains(card.id))
            ) {
                activity.runOnUiThread {
                    Handler(Looper.myLooper()!!).postDelayed({
                        activate()
                    }, 1500)
                }
            }
        }
    }

    fun onDestroy() {
        game.stateChangedListeners.remove(card.name)
    }
}
