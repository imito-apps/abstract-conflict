package imito.ac.entities.cards.sets.main.base

import imito.ac.entities.cards.*
import imito.ac.entities.game.*
import imito.ac.entities.player.*

class Defence(
    isShown: Boolean = false,
) : Card(Id, 2, 4, isShown) {
    companion object {
        const val Id = 4
    }

    override fun playSelf(gameState: GameState, decision: PlayerDecision): GameState {
        return changeMarksOfCurrent(gameState) {
            it.withDefence(true)
        }
    }
}
