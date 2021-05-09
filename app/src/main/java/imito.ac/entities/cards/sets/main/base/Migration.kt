package imito.ac.entities.cards.sets.main.base

import imito.ac.R
import imito.ac.entities.cards.*
import imito.ac.entities.game.*
import imito.ac.entities.player.*

class Migration(
    isShown: Boolean = false,
) : Card(Id, 2, 5, isShown) {
    companion object {
        const val Id = 5
    }

    override fun playSelf(gameState: GameState, decision: PlayerDecision): GameState {
        val targetPlayer = getTargetPlayer(gameState, decision)
        var notification = getNotificationWithTarget(gameState, targetPlayer)
        if (cannotTargetPlayer(targetPlayer)) return gameState.withNotification(notification)

        val targetCard = targetPlayer!!.card
        return if (DominationBase.Ids.contains(targetCard.id))
            gameState.eliminatePlayer(targetPlayer.id)
                .withNotification(notification
                    .appendAll(R.string.message_domination_stays))
        else {
            notification = if (targetPlayer.id == gameState.currentPlayerId) notification
            else notification.appendAll(R.string.message_domination_leaves)
            gameState
                .discardFrom(targetPlayer.id, targetCard)
                .dealCardTo(targetPlayer.id)
                .withNotification(notification)
        }
    }
}
