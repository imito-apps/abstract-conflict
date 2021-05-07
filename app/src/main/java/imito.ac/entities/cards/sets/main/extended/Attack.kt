package imito.ac.entities.cards.sets.main.extended

import imito.ac.entities.cards.sets.main.base.*

class Attack(
    isShown: Boolean = false,
) : AttackBase(Id, 3, isShown) {
    companion object {
        const val Id = 11
    }
}
