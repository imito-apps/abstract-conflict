package imito.ac.models.cards

import android.content.res.*
import androidx.appcompat.app.*
import imito.core.id.*
import imito.ac.*
import imito.ac.activities.dialogs.card.*
import imito.ac.entities.cards.*
import imito.ac.entities.player.*
import imito.ac.game.*
import imito.ac.models.*
import java.util.*

abstract class CardModel(
    entity: Card,
    resources: Resources,
    descriptionId: Int,
    nameId: Int,
    val imageId: Int,
) : IIntIdentifiable {
    override val id = entity.id
    val count = entity.count
    val description = resources.getString(descriptionId)
    val isShown = entity.isShown
    val name = findName(nameId, resources)
    val points = entity.points

    open fun playSelf(
        activity: AppCompatActivity,
        game: Game,
        onEnd: () -> Unit,
    ) {
        ConfirmCardDialog(activity, this, onEnd, game, {
            game.makeDecision(PlayerDecision(id))
            onEnd()
        })
    }

    protected fun selectAnyPlayer(
        activity: AppCompatActivity,
        game: Game,
        onEnd: () -> Unit,
        onSelect: ((UUID) -> Unit)? = null,
    ) = selectPlayerFrom(game.players, activity, game, onEnd, onSelect ?: {})

    protected fun selectOtherPlayer(
        activity: AppCompatActivity,
        game: Game,
        onEnd: () -> Unit,
        onSelect: ((UUID) -> Unit)? = null,
    ) = selectPlayerFrom(game.otherPlayers, activity, game, onEnd, onSelect ?: {})

    private fun selectPlayerFrom(
        players: PlayerModels,
        activity: AppCompatActivity,
        game: Game,
        onEnd: () -> Unit,
        onSelect: (UUID) -> Unit,
    ) {
        PlayerSelectDialog(activity, this, onEnd, game, players) { playerId ->
            onSelect(playerId)
            val decision = PlayerDecision(this.id, playerId)
            game.makeDecision(decision)
            onEnd()
        }
    }

    private fun findName(nameId: Int, resources: Resources): String {
        val id = if (isShown) nameId
        else R.string.card_name_hidden

        return resources.getString(id)
    }

    class None(resources: Resources) :
        CardModel(Card.None(), resources, R.string.card_name_hidden, R.string.card_name_hidden, R.drawable.ic_card_conflict)
}
