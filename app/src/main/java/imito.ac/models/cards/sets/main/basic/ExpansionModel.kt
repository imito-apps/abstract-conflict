package imito.ac.models.cards.sets.main.basic

import android.content.res.*
import imito.ac.*
import imito.ac.entities.cards.*
import imito.ac.models.cards.*

class ExpansionModel(
    entity: Card,
    resources: Resources,
) : CardModel(
    entity,
    resources,
    R.string.card_description_expansion,
    R.string.card_name_expansion,
    R.drawable.ic_card_expansion,
)
