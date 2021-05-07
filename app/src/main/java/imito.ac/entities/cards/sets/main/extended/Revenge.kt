package imito.ac.entities.cards.sets.main.extended

import imito.ac.entities.cards.*

class Revenge(
    isShown: Boolean = false,
) : Card(Id, 1, 6, isShown) {
    companion object {
        const val Id = 26
    }
}
