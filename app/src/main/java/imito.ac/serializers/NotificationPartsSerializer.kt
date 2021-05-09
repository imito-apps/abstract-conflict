package imito.ac.serializers

import imito.core.collections.*
import imito.ac.entities.game.notifications.*

private const val Separator = "\u0012"

fun NotificationParts.serialize() = this.joinToString(Separator) { it.serialize() }

fun NotificationParts.Companion.deserialize(text: String): NotificationParts {
    val list = ChainableListBase.deserialize(Separator, text) {
        NotificationPart.deserialize(it)
    }
    return NotificationParts(list)
}
