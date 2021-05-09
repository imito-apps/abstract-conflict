package imito.ac.entities.cards

import imito.ac.entities.cards.sets.*
import imito.ac.entities.cards.sets.main.base.*
import imito.ac.entities.cards.sets.main.basic.*
import imito.ac.entities.cards.sets.main.basic.Attack
import imito.ac.entities.cards.sets.main.basic.Domination
import imito.ac.entities.cards.sets.main.basic.Exploitation
import imito.ac.entities.cards.sets.main.basic.Trade
import imito.ac.entities.cards.sets.main.extended.*

class CardFactory {
    companion object {
        fun getCollection(isBasic: Boolean) =
            if (isBasic) BasicCards.instance else ExtendedCards.instance

        fun create(id: Int, isShown: Boolean): Card {
            return when (id) {
                Expansion.Id -> Expansion(isShown)
                Attack.Id -> Attack(isShown)
                Espionage.Id -> Espionage(isShown)
                Capitalism.Id -> Capitalism(isShown)
                Defence.Id -> Defence(isShown)
                Migration.Id -> Migration(isShown)
                Trade.Id -> Trade(isShown)
                Strategy.Id -> Strategy(isShown)
                Exploitation.Id -> Exploitation(isShown)
                Domination.Id -> Domination(isShown)
                imito.ac.entities.cards.sets.main.extended.Attack.Id ->
                    imito.ac.entities.cards.sets.main.extended.Attack(isShown)
                imito.ac.entities.cards.sets.main.extended.Trade.Id ->
                    imito.ac.entities.cards.sets.main.extended.Trade(isShown)
                imito.ac.entities.cards.sets.main.extended.Exploitation.Id ->
                    imito.ac.entities.cards.sets.main.extended.Exploitation(isShown)
                imito.ac.entities.cards.sets.main.extended.Domination.Id ->
                    imito.ac.entities.cards.sets.main.extended.Domination(isShown)
                Counterstrike.Id -> Counterstrike(isShown)
                Parasitism.Id -> Parasitism(isShown)
                Conspiracy.Id -> Conspiracy(isShown)
                Infiltration.Id -> Infiltration(isShown)
                Allegation.Id -> Allegation(isShown)
                Investment.Id -> Investment(isShown)
                Revenge.Id -> Revenge(isShown)
                Deception.Id -> Deception(isShown)
                Fortune.Id -> Fortune(isShown)
                else -> Card.none
            }
        }
    }
}
