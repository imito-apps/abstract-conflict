package imito.ac.entities.cards.sets.main.base

import imito.ac.R
import imito.ac.entities.cards.*
import imito.ac.entities.game.*
import imito.ac.entities.player.*

class Espionage(
    isShown: Boolean = false,
) : Card(Id, 2, 2, isShown) {
    companion object {
        const val Id = 2
    }

    override fun playSelf(gameState: GameState, decision: PlayerDecision): GameState {
        val targetPlayer = getTargetPlayer(gameState, decision)
        val notification = getNotificationToOthers(gameState)

        return if (cannotTargetPlayer(targetPlayer)) gameState.withNotification(notification)
        else gameState.withNotification(notification
            .withToPrevious(R.string.message_card_revealed, targetPlayer?.name ?: "", "${targetPlayer?.card?.id}"))
    }
}
