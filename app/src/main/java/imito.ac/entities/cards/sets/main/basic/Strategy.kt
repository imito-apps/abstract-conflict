package imito.ac.entities.cards.sets.main.basic

import imito.ac.entities.cards.*
import imito.ac.entities.game.*
import imito.ac.entities.player.*

class Strategy(
    isShown: Boolean = false,
) : Card(Id, 2, 6, isShown) {
    companion object {
        const val Id = 6
        const val CardToDrawCount = 2
    }

    override fun playSelf(gameState: GameState, decision: PlayerDecision): GameState {
        val stockCards = gameState.stockPile
            .take(CardToDrawCount)
            .toList()
        val allCards = createAllCards(gameState, stockCards)
        val newPlayer = createNewPlayer(decision, allCards, gameState)
        val cardsToBottom = createCardsToBottom(allCards, stockCards, decision)
        val newStock = createNewStock(gameState, stockCards, cardsToBottom)
        val notification = getNotificationToOthers(gameState)
        return gameState
            .withNotification(notification)
            .withCurrentPlayer(newPlayer)
            .withPiles(gameState.discardPile, newStock)
    }

    private fun createAllCards(
        gameState: GameState,
        stockCards: List<Card>,
    ): MutableList<Card> {
        val secondCard = gameState.currentPlayer.cards
            .firstOrNull { it.id != id } ?: this
        val allCards = stockCards.toMutableList()
        allCards.add(secondCard)
        return allCards
    }

    private fun createNewPlayer(
        decision: PlayerDecision,
        allCards: MutableList<Card>,
        gameState: GameState,
    ): Player {
        val handCardId = decision.value1.toIntOrNull()
            ?: allCards.first().id
        val index = allCards.indexOfFirst { it.id == handCardId }
        val handCard = allCards.removeAt(index)
            .toShown()
        return gameState.currentPlayer
            .withCards(Cards(listOf(handCard)))
    }

    private fun createCardsToBottom(
        allCards: MutableList<Card>,
        stockCards: List<Card>,
        decision: PlayerDecision,
    ): MutableList<Card> {
        val cardsToBottom = allCards.toMutableList()
        if (stockCards.size == CardToDrawCount) {
            val bottomCardId = decision.value2.toIntOrNull()
                ?: cardsToBottom.first().id
            if (cardsToBottom.first().id == bottomCardId) {
                val bottomCard = cardsToBottom.removeFirst()
                cardsToBottom.add(bottomCard)
            }
        }
        return cardsToBottom
    }

    private fun createNewStock(
        gameState: GameState,
        stockCards: List<Card>,
        cardsToBottom: MutableList<Card>,
    ): Cards {
        var newStock = gameState.stockPile
        for (card in stockCards) {
            newStock = newStock.removeOne(card)
        }
        newStock = newStock.addAsLast(cardsToBottom)
        return newStock
    }
}
