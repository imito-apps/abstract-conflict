package imito.ac.entities.cards.sets

import imito.ac.entities.cards.sets.main.base.*
import imito.ac.entities.cards.sets.main.basic.*

class BasicCards : CardCollection(listOf(
    Expansion(true),
    Attack(true),
    Espionage(true),
    Capitalism(true),
    Defence(true),
    Migration(true),
    Strategy(true),
    Trade(true),
    Exploitation(true),
    Domination(true),
)) {
    companion object {
        val instance = BasicCards()
    }
}
