package imito.ac.entities.cards.sets.main.extended

import imito.ac.entities.cards.*

class Counterstrike(
    isShown: Boolean = false,
) : Card(Id, 1, 0, isShown) {
    companion object {
        const val Id = 20
    }
}
