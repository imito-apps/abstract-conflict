package imito.ac.models.cards

import android.content.res.*
import imito.core.collections.*
import imito.ac.entities.cards.*

class CardModels(
    value: List<CardModel>,
) : ChainableList<CardModel, Int, CardModels>(value) {
    override fun createSelf(value: List<CardModel>) = CardModels(value)
    val first = value.firstOrNull()

    override fun iterator(): Iterator<CardModel> = value.iterator()

    companion object {
        fun from(cards: Cards, resources: Resources) = CardModels(cards.map { CardModelFactory.create(it, resources) })
    }
}
