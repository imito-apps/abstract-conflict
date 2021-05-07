package imito.ac.entities.cards.sets

import imito.ac.entities.cards.sets.main.base.*
import imito.ac.entities.cards.sets.main.extended.*

class ExtendedCards : CardCollection(listOf(
    Counterstrike(true), Parasitism(true),
    Attack(true),
    Espionage(true), Conspiracy(true),
    Capitalism(true), Infiltration(true),
    Defence(true), Allegation(true),
    Migration(true), Investment(true),
    Trade(true), Revenge(true),
    Exploitation(true), Deception(true),
    Domination(true),
    Fortune(true),
)) {
    companion object {
        val instance = ExtendedCards()
    }
}
