package imito.ac.models.cards.sets.main.extended

import android.content.res.*
import androidx.appcompat.app.*
import imito.ac.*
import imito.ac.entities.cards.*
import imito.ac.game.*
import imito.ac.models.cards.*

class ConspiracyModel(
    entity: Card,
    resources: Resources,
) : CardModel(
    entity,
    resources,
    R.string.card_description_conspiracy,
    R.string.card_name_conspiracy,
    R.drawable.ic_card_conspiracy,
) {
    override fun playSelf(
        activity: AppCompatActivity,
        game: Game,
        onEnd: () -> Unit,
    ) {
        super.playSelf(activity, game, onEnd)
    }
}
