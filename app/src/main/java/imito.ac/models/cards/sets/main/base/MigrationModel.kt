package imito.ac.models.cards.sets.main.base

import android.content.res.*
import androidx.appcompat.app.*
import imito.ac.*
import imito.ac.entities.cards.*
import imito.ac.game.*
import imito.ac.models.cards.*

class MigrationModel(
    entity: Card,
    resources: Resources,
) : CardModel(
    entity,
    resources,
    R.string.card_description_migration,
    R.string.card_name_migration,
    R.drawable.ic_card_migration,
) {
    override fun playSelf(
        activity: AppCompatActivity,
        game: Game,
        onEnd: () -> Unit,
    ) {
        selectAnyPlayer(activity, game, onEnd)
    }
}
