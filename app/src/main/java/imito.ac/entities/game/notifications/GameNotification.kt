package imito.ac.entities.game.notifications

class GameNotification(
    val toOthers: NotificationParts = NotificationParts(listOf()),
    val toPrevious: NotificationParts = NotificationParts(listOf()),
) {
    companion object

    fun appendToOthers(textId: Int, arg1: String = "", arg2: String = ""): GameNotification {
        val part = NotificationPart(textId, arg1, arg2)
        return GameNotification(toOthers.addAsLast(part), toPrevious)
    }

    fun appendAll(textId: Int, arg1: String = "", arg2: String = ""): GameNotification {
        val part = NotificationPart(textId, arg1, arg2)
        return GameNotification(toOthers.addAsLast(part), toPrevious.addAsLast(part))
    }

    fun withToPrevious(textId: Int, arg1: String = "", arg2: String = ""): GameNotification {
        val part = NotificationPart(textId, arg1, arg2)
        return GameNotification(toOthers, toPrevious.clearAndAdd(part))
    }
}
