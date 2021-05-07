package imito.ac.entities.cards.sets.main.extended

import imito.ac.entities.cards.*

class Infiltration(
    isShown: Boolean = false,
) : Card(Id, 2, 3, isShown) {
    companion object {
        const val Id = 23
    }
}
