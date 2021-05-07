package imito.ac.models.cards.sets.main.base

import android.content.res.*
import imito.ac.*
import imito.ac.entities.cards.*
import imito.ac.models.cards.*

class DefenceModel(
    entity: Card,
    resources: Resources,
) : CardModel(
    entity,
    resources,
    R.string.card_description_defence,
    R.string.card_name_defence,
    R.drawable.ic_card_defence,
)
