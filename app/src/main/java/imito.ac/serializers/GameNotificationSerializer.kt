package imito.ac.serializers

import imito.ac.entities.game.notifications.*

private const val Separator = '\u0009'

fun GameNotification.serialize() = "${toOthers.serialize()}$Separator${toPrevious.serialize()}"

fun GameNotification.Companion.deserialize(text: String): GameNotification {
    val properties = text.split(Separator)

    val toOthers = NotificationParts.deserialize(properties[0])
    val toPrevious = NotificationParts.deserialize(properties[1])

    return GameNotification(toOthers, toPrevious)
}
