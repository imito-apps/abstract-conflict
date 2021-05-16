package imito.ac.models.cards.sets.main.base

import android.content.res.*
import androidx.appcompat.app.*
import imito.ac.*
import imito.ac.activities.adapters.*
import imito.ac.activities.dialogs.*
import imito.ac.activities.dialogs.card.*
import imito.ac.entities.cards.*
import imito.ac.entities.player.*
import imito.ac.game.*
import imito.ac.models.cards.*

open class AttackModel(
    entity: Card,
    val resources: Resources,
) : CardModel(
    entity,
    resources,
    R.string.card_description_attack,
    R.string.card_name_attack,
    R.drawable.ic_card_attack,
) {
    override fun playSelf(
        activity: AppCompatActivity,
        game: Game,
        onEnd: () -> Unit,
    ) {
        val texts = getTexts(game)
        PlayerSelectDialog(activity, this, onEnd, game, game.otherPlayers) { playerId ->
            fun makeDecision(points: Int = -1) {
                val decision = PlayerDecision(id, playerId, points)
                game.makeDecision(decision)
                onEnd()
            }
            if (game.otherPlayers.canAnyBeTargeted) {
                TextSelectDialog(activity, R.string.label_select_card, texts, onEnd) { points ->
                    makeDecision(points)
                }
            } else {
                makeDecision()
            }
        }
    }

    private fun getTexts(game: Game): List<TextAdapter.KeyValue> {
        return game.cardsForPoint
            .filter { it.key != points }
            .map { kv ->
                val text = kv.value.joinToString(", ") { it.name }
                TextAdapter.KeyValue(kv.key, text)
            }
    }
}
