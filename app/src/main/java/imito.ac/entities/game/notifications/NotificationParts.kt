package imito.ac.entities.game.notifications

import imito.core.collections.*

class NotificationParts(
    value: List<NotificationPart>,
) : ChainableListBase<NotificationPart, NotificationParts>(value) {
    override fun createSelf(value: List<NotificationPart>) = NotificationParts(value)

    companion object
}
