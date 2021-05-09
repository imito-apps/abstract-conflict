package imito.ac.serializers

import imito.ac.entities.player.*

private const val Separator = '\u0008'

fun PlayerDecision.serialize() = "$cardId$Separator$value1$Separator$value2"

fun PlayerDecision.Companion.deserialize(text: String): PlayerDecision {
    val properties = text.split(Separator)
    val cardId = properties[0].toInt()
    return PlayerDecision(cardId, properties[1], properties[2])
}
