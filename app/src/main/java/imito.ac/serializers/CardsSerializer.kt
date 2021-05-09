package imito.ac.serializers

import imito.core.collections.*
import imito.ac.entities.cards.*

private const val Separator = "\u0004"

fun Cards.serialize() = this.joinToString(Separator) { it.serialize() }

fun Cards.Companion.deserialize(text: String): Cards {
    val list = ChainableListBase.deserialize(Separator, text) {
        Card.deserialize(it)
    }
    return Cards(list)
}
