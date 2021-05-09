package imito.ac.entities.cards.sets.main.extended

import imito.ac.entities.cards.*

class Fortune(
    isShown: Boolean = false,
) : Card(Id, 1, 9, isShown) {
    companion object {
        const val Id = 29
    }
}
