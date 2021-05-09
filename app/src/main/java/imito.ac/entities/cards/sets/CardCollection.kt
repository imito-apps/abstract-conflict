package imito.ac.entities.cards.sets

import imito.ac.entities.cards.*

open class CardCollection(
    cards: List<Card>,
) {
    val uniqueValues = Cards(cards)
    val deck = createDeckFrom(uniqueValues)

    companion object {
        fun createDeckFrom(cards: Cards): Cards {
            val deck = cards.flatMap { card ->
                MutableList(card.count) { card }
            }
            return Cards(deck)
        }
    }
}
