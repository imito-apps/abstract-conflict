package imito.ac.entities.cards.sets.main.basic

import imito.ac.entities.cards.*
import imito.ac.entities.game.*
import imito.ac.entities.player.*

class Expansion(
    isShown: Boolean = false,
) : Card(Id, 2, 0, isShown) {
    companion object {
        const val Id = 10
    }

    override fun playSelf(gameState: GameState, decision: PlayerDecision): GameState {
        return changeMarksOfCurrent(gameState) {
            it.withExpansion()
        }
    }
}
