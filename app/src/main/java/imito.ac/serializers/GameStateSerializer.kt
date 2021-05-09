package imito.ac.serializers

import imito.ac.entities.cards.*
import imito.ac.entities.game.*
import imito.ac.entities.game.notifications.*
import imito.ac.entities.player.*
import java.util.*

private const val Separator = '\u0003'

fun GameState.serialize() = "$currentPlayerId" +
        "$Separator${players.serialize()}" +
        "$Separator${discardPile.serialize()}" +
        "$Separator${stockPile.serialize()}" +
        "$Separator${notification.serialize()}" +
        "$Separator$orderNumber" +
        "$Separator$isBasicDeck"

fun GameState.Companion.deserialize(text: String): GameState {
    val properties = text.split(Separator)
    val currentId = UUID.fromString(properties[0])
    val players = Players.deserialize(properties[1])
    val discardPile = Cards.deserialize(properties[2])
    val stockPile = Cards.deserialize(properties[3])
    val notification = GameNotification.deserialize(properties[4])
    val orderNumber = properties[5].toInt()
    val isBasic = properties[6].toBoolean()
    return GameState(currentId, players, discardPile, stockPile, notification, orderNumber, isBasic)
}
