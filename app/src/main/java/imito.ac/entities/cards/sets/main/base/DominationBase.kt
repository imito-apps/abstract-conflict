package imito.ac.entities.cards.sets.main.base

import imito.ac.entities.cards.*
import imito.ac.entities.cards.sets.main.extended.*

open class DominationBase(
    id: Int,
    points: Int,
    isShown: Boolean,
) : Card(id, 1, points, isShown) {
    companion object {
        val Ids = arrayOf(imito.ac.entities.cards.sets.main.basic.Domination.Id, Domination.Id)
    }
}
