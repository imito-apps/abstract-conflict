package imito.ac.entities.cards

import imito.core.collections.*

class Cards(
    value: List<Card>,
) : ChainableList<Card, Int, Cards>(value) {
    override fun createSelf(value: List<Card>) = Cards(value)

    companion object

    val first = value.firstOrNull() ?: Card.none
    val last = value.lastOrNull() ?: Card.none
}
