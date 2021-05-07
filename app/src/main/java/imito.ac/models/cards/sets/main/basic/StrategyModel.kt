package imito.ac.models.cards.sets.main.basic

import android.content.res.*
import androidx.appcompat.app.*
import imito.ac.*
import imito.ac.activities.adapters.*
import imito.ac.activities.dialogs.*
import imito.ac.activities.dialogs.card.*
import imito.ac.entities.cards.*
import imito.ac.entities.cards.sets.main.basic.*
import imito.ac.entities.player.*
import imito.ac.game.*
import imito.ac.models.cards.*

class StrategyModel(
    entity: Card,
    val resources: Resources,
) : CardModel(
    entity,
    resources,
    R.string.card_description_strategy,
    R.string.card_name_strategy,
    R.drawable.ic_card_strategy,
) {
    override fun playSelf(
        activity: AppCompatActivity,
        game: Game,
        onEnd: () -> Unit,
    ) {
        ConfirmCardDialog(activity, this, onEnd, game, {
            val texts = getTexts(game)
            TextSelectDialog(activity, resources, R.string.label_select_card_for_you, texts, null) { handCardId ->
                fun makeDecision(bottomCardId: Int = -1) {
                    val decision = PlayerDecision(id, handCardId, bottomCardId)
                    game.makeDecision(decision)
                    onEnd()
                }
                if (texts.size <= Strategy.CardToDrawCount)
                    makeDecision()
                else {
                    val index = texts.indexOfFirst { it.key == handCardId }
                    texts.removeAt(index)
                    TextSelectDialog(activity, resources, R.string.label_select_card_for_bottom, texts, null) { bottomCardId ->
                        makeDecision(bottomCardId)
                    }
                }
            }
        })
    }

    private fun getTexts(game: Game): MutableList<TextAdapter.KeyValue> {
        val secondCard = game.playerCards
            .firstOrNull { it.id != id } ?: this@StrategyModel
        return sequence {
            yield(secondCard)
            yieldAll(game.stateModel!!.stockPile.take(Strategy.CardToDrawCount))
        }
            .map { TextAdapter.KeyValue(it.id, it.name) }
            .toMutableList()
    }
}
