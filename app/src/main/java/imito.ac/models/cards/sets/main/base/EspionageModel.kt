package imito.ac.models.cards.sets.main.base

import android.content.res.*
import androidx.appcompat.app.*
import imito.ac.*
import imito.ac.entities.cards.*
import imito.ac.game.*
import imito.ac.models.cards.*

class EspionageModel(
    entity: Card,
    resources: Resources,
) : CardModel(
    entity,
    resources,
    R.string.card_description_espionage,
    R.string.card_name_espionage,
    R.drawable.ic_card_espionage,
) {
    override fun playSelf(
        activity: AppCompatActivity,
        game: Game,
        onEnd: () -> Unit,
    ) {
        selectOtherPlayer(activity, game, onEnd)
    }
}
