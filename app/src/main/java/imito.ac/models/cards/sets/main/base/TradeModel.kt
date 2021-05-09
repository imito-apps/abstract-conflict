package imito.ac.models.cards.sets.main.base

import android.content.res.*
import androidx.appcompat.app.*
import imito.ac.*
import imito.ac.entities.cards.*
import imito.ac.game.*
import imito.ac.models.cards.*

class TradeModel(
    entity: Card,
    resources: Resources,
) : CardModel(
    entity,
    resources,
    R.string.card_description_trade,
    R.string.card_name_trade,
    R.drawable.ic_card_trade,
) {
    override fun playSelf(
        activity: AppCompatActivity,
        game: Game,
        onEnd: () -> Unit,
    ) {
        selectOtherPlayer(activity, game, onEnd)
    }
}
