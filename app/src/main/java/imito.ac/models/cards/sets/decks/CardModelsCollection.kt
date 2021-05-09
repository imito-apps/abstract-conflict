package imito.ac.models.cards.sets.decks

import android.content.res.*
import imito.ac.entities.cards.sets.*
import imito.ac.models.cards.*

open class CardModelsCollection(
    entity: CardCollection,
    resources: Resources,
) {
    val uniqueValues = CardModels(entity
        .uniqueValues.map { CardModelFactory.create(it, resources) }
    )
    val valuesForPoint = mapCardsForPoint(uniqueValues)

    companion object {
        fun mapCardsForPoint(cards: CardModels): Map<Int, CardModels> {
            val map = mutableMapOf<Int, CardModels>()
            for (card in cards) {
                val cardModels = (map[card.points] ?: CardModels(listOf()))
                    .addAsLast(card)
                map[card.points] = cardModels
            }
            return map
        }
    }
}
