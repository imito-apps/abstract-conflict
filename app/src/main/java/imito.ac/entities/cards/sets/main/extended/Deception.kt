package imito.ac.entities.cards.sets.main.extended

import imito.ac.entities.cards.*

class Deception(
    isShown: Boolean = false,
) : Card(Id, 1, 7, isShown) {
    companion object {
        const val Id = 27
    }
}
