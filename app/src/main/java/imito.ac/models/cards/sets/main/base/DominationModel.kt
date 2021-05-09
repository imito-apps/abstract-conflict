package imito.ac.models.cards.sets.main.base

import android.content.res.*
import androidx.appcompat.app.*
import imito.ac.*
import imito.ac.activities.dialogs.card.*
import imito.ac.entities.cards.*
import imito.ac.game.*
import imito.ac.models.cards.*

class DominationModel(
    entity: Card,
    resources: Resources,
) : CardModel(
    entity,
    resources,
    R.string.card_description_domination,
    R.string.card_name_domination,
    R.drawable.ic_card_domination,
) {
    override fun playSelf(activity: AppCompatActivity, game: Game, onEnd: () -> Unit) {
        ConfirmCardDialog(activity, this, onEnd, game, { }, true)
    }
}
