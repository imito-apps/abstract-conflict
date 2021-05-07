package imito.ac.entities.cards.sets.main.extended

import imito.ac.entities.cards.*

class Conspiracy(
    isShown: Boolean = false,
) : Card(Id, 2, 2, isShown) {
    companion object {
        const val Id = 22
    }
}
