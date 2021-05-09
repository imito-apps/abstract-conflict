package imito.ac.entities.cards.sets.main.basic

import imito.ac.entities.cards.sets.main.base.*

class Trade(
    isShown: Boolean = false,
) : TradeBase(Id, 7, isShown) {
    companion object {
        const val Id = 7
    }
}
