package imito.ac.models.cards

import android.content.res.*
import imito.ac.entities.cards.*
import imito.ac.entities.cards.sets.main.base.*
import imito.ac.entities.cards.sets.main.basic.*
import imito.ac.entities.cards.sets.main.extended.Trade
import imito.ac.entities.cards.sets.main.extended.Conspiracy
import imito.ac.entities.cards.sets.main.extended.Domination
import imito.ac.entities.cards.sets.main.extended.Counterstrike
import imito.ac.models.cards.sets.decks.*
import imito.ac.models.cards.sets.main.base.*
import imito.ac.models.cards.sets.main.basic.*
import imito.ac.models.cards.sets.main.extended.*

class CardModelFactory {
    companion object {
        fun getCollection(isBasic: Boolean, resources: Resources) =
            if (isBasic) BasicCardModels.getInstance(resources)
            else ExtendedCardModels.getInstance(resources)

        fun create(id: Int, resources: Resources, isShown: Boolean): CardModel {
            val card = CardFactory.create(id, isShown)
            return create(card, resources)
        }

        fun create(card: Card, resources: Resources): CardModel {
            return when (card.id) {
                Expansion.Id -> ExpansionModel(card, resources)
                Capitalism.Id -> CapitalismModel(card, resources)
                Defence.Id -> DefenceModel(card, resources)
                Attack.Id -> AttackModel(card, resources)
                imito.ac.entities.cards.sets.main.extended.Attack.Id -> AttackModel(card, resources)
                Exploitation.Id -> ExploitationModel(card, resources)
                imito.ac.entities.cards.sets.main.extended.Exploitation.Id -> ExploitationModel(card, resources)
                imito.ac.entities.cards.sets.main.basic.Domination.Id -> DominationModel(card, resources)
                Domination.Id -> DominationModel(card, resources)
                imito.ac.entities.cards.sets.main.basic.Trade.Id -> TradeModel(card, resources)
                Trade.Id -> TradeModel(card, resources)
                Espionage.Id -> EspionageModel(card, resources)
                Migration.Id -> MigrationModel(card, resources)
                Strategy.Id -> StrategyModel(card, resources)

                Conspiracy.Id -> ConspiracyModel(card, resources)
                Counterstrike.Id -> CounterstrikeModel(card, resources)
//                Parasitism.Id -> ParasitismModel(card, resources)
//                Infiltration.Id -> InfiltrationModel(card, resources)
//                Allegation.Id -> AllegationModel(card, resources)
//                Investment.Id -> InvestmentModel(card, resources)
//                Revenge.Id -> RevengeModel(card, resources)
//                Deception.Id -> DeceptionModel(card, resources)
//                Fortune.Id -> FortuneModel(card, resources)
                else -> CardModel.None(resources)

                //else -> throw IllegalArgumentException("${card.id}")
            }
        }
    }
}
