package imito.ac.entities.cards.sets.main.base

import imito.ac.R
import imito.ac.entities.cards.*
import imito.ac.entities.game.*
import imito.ac.entities.player.*

open class AttackBase(
    id: Int,
    count: Int,
    isShown: Boolean,
) : Card(id, count, Points, isShown) {
    companion object {
        const val Points = 1
    }

    override fun playSelf(gameState: GameState, decision: PlayerDecision): GameState {
        val targetCardPoints = decision.targetNumber
        val targetPlayer = getTargetPlayer(gameState, decision)
        var notification = getNotificationWithTarget(gameState, targetPlayer)
        if (cannotTargetPlayer(targetPlayer)) return gameState.withNotification(notification)

        notification = notification.appendToOthers(R.string.message_card_targeted, targetCardPoints.toString())
        return if (targetCardPoints == Points
            || targetPlayer!!.cards.none { it.points == targetCardPoints }
        ) {
            gameState.withNotification(notification
                .appendAll(R.string.message_targeted_stays))
        } else
            gameState.eliminatePlayer(targetPlayer.id)
                .withNotification(notification
                    .appendAll(R.string.message_targeted_leaves))
    }
}
