package imito.ac.entities.cards.sets.main.base

import imito.ac.R
import imito.ac.entities.cards.*
import imito.ac.entities.game.*
import imito.ac.entities.player.*

class Capitalism(
    isShown: Boolean = false,
) : Card(Id, 2, 3, isShown) {
    companion object {
        const val Id = 3
    }

    override fun playSelf(gameState: GameState, decision: PlayerDecision): GameState {
        val targetPlayer = getTargetPlayer(gameState, decision)
        val notification = getNotificationWithTarget(gameState, targetPlayer)
        if (cannotTargetPlayer(targetPlayer)) return gameState.withNotification(notification)

        val targetPlayerPoints = targetPlayer!!.card.points
        val currentPlayerPoints = gameState.currentPlayer.card.points
        return if (targetPlayerPoints == currentPlayerPoints)
            gameState.withNotification(notification
                .appendAll(R.string.message_draw))
        else {
            val eliminatedPlayer = if (targetPlayerPoints < currentPlayerPoints) targetPlayer
            else gameState.currentPlayer
            gameState.eliminatePlayer(eliminatedPlayer.id)
                .withNotification(notification
                    .appendAll(R.string.message_lower_leaves, eliminatedPlayer.name))
        }
    }
}
