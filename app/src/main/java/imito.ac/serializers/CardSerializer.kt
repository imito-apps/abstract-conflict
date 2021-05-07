package imito.ac.serializers

import imito.ac.entities.cards.*

private const val Separator = '\u0005'

fun Card.serialize() = "$id$Separator$isShown"

fun Card.Companion.deserialize(text: String): Card {
    val properties = text.split(Separator)
    return CardFactory.create(properties[0].toInt(), properties[1].toBoolean())
}
