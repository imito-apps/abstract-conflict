package imito.ac.entities.cards.sets.main.extended

import imito.ac.entities.cards.*

class Allegation(
    isShown: Boolean = false,
) : Card(Id, 2, 4, isShown) {
    companion object {
        const val Id = 24
    }
}
