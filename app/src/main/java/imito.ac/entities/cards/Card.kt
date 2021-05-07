package imito.ac.entities.cards

import imito.core.id.*
import imito.ac.R
import imito.ac.entities.game.*
import imito.ac.entities.game.notifications.*
import imito.ac.entities.player.*

abstract class Card(
    override val id: Int,
    val count: Int,
    val points: Int,
    val isShown: Boolean,
) : IIntIdentifiable {
    companion object {
        val none = None()
    }

    fun toHidden(): Card {
        return CardFactory.create(id, false)
    }

    fun toShown(): Card {
        return CardFactory.create(id, true)
    }

    open fun playSelf(gameState: GameState, decision: PlayerDecision) = withNotificationToOthers(gameState)

    protected fun changeMarksOfCurrent(
        gameState: GameState,
        func: (PlayerMarks) -> PlayerMarks,
    ): GameState {
        return withNotificationToOthers(gameState)
            .changeMarksOfCurrent(func)
    }

    protected fun withNotificationToOthers(gameState: GameState): GameState {
        return gameState.withNotification(getNotificationToOthers(gameState))
    }

    protected fun getTargetPlayer(gameState: GameState, decision: PlayerDecision): Player? {
        return gameState.players
            .firstOrNull { it.id == decision.getTargetPlayerId() }
    }

    protected fun getNotificationToOthers(gameState: GameState) =
        GameNotification().appendToOthers(R.string.message_main, gameState.currentPlayer.name, "$id")

    protected fun getNotificationWithTarget(gameState: GameState, targetPlayer: Player?): GameNotification {
        val notification = getNotificationToOthers(gameState)
        return if (targetPlayer == null) notification
        else notification.appendToOthers(R.string.message_player_targeted, targetPlayer.name)
    }

    protected fun cannotTargetPlayer(player: Player?) = player == null || player.isEliminated || player.hadDefence

    class None : Card(-1, -1, -1, false)
}
