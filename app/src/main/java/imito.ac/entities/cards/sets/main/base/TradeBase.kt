package imito.ac.entities.cards.sets.main.base

import imito.ac.entities.cards.*
import imito.ac.entities.cards.sets.main.extended.*
import imito.ac.entities.game.*
import imito.ac.entities.player.*

open class TradeBase(
    id: Int,
    points: Int,
    isShown: Boolean,
) : Card(id, 1, points, isShown) {
    companion object {
        val Ids = arrayOf(imito.ac.entities.cards.sets.main.basic.Trade.Id, Trade.Id)
    }

    override fun playSelf(gameState: GameState, decision: PlayerDecision): GameState {
        val targetPlayer = getTargetPlayer(gameState, decision)
        val notification = getNotificationWithTarget(gameState, targetPlayer)
        val newState = gameState.withNotification(notification)
        return if (cannotTargetPlayer(targetPlayer)) newState
        else newState.tradeCards(gameState.currentPlayerId, targetPlayer!!.id)
    }
}
