package imito.ac.serializers

import imito.ac.entities.game.notifications.*

private const val Separator = '\u0011'

fun NotificationPart.serialize() = "$textId$Separator$arg1$Separator$arg2"

fun NotificationPart.Companion.deserialize(text: String): NotificationPart {
    val properties = text.split(Separator)
    return NotificationPart(properties[0].toInt(), properties[1], properties[2])
}
