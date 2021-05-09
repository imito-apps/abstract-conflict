package imito.ac.models.cards.sets.main.extended

import android.content.res.*
import imito.ac.*
import imito.ac.entities.cards.*
import imito.ac.models.cards.*

class CounterstrikeModel(
    entity: Card,
    resources: Resources,
) : CardModel(
    entity,
    resources,
    R.string.card_description_counterstrike,
    R.string.card_name_counterstrike,
    R.drawable.ic_card_counterstrike,
)
