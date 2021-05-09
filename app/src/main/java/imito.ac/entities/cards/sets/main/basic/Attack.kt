package imito.ac.entities.cards.sets.main.basic

import imito.ac.entities.cards.sets.main.base.*

class Attack(
    isShown: Boolean = false,
) : AttackBase(Id, 6, isShown) {
    companion object {
        const val Id = 1
    }
}
