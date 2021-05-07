package imito.ac.entities.cards.sets.main.extended

import imito.ac.entities.cards.sets.main.base.*

class Domination(
    isShown: Boolean = false,
) : DominationBase(Id, 8, isShown) {
    companion object {
        const val Id = 18
    }
}
