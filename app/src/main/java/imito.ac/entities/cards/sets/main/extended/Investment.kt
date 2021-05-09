package imito.ac.entities.cards.sets.main.extended

import imito.ac.entities.cards.*

class Investment(
    isShown: Boolean = false,
) : Card(Id, 2, 5, isShown) {
    companion object {
        const val Id = 25
    }
}
