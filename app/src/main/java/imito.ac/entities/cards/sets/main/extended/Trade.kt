package imito.ac.entities.cards.sets.main.extended

import imito.ac.entities.cards.sets.main.base.*

class Trade(
    isShown: Boolean = false,
) : TradeBase(Id, 6, isShown) {
    companion object {
        const val Id = 16
    }
}
