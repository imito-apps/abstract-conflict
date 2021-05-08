package imito.ac.entities.game

import imito.core.*
import imito.ac.entities.cards.*
import imito.ac.entities.cards.sets.main.base.*
import imito.ac.entities.cards.sets.main.basic.*
import imito.ac.entities.game.notifications.*
import imito.ac.entities.player.*
import java.util.*

class GameState(
    val currentPlayerId: UUID,
    val players: Players,
    val discardPile: Cards,
    val stockPile: Cards,
    val notification: GameNotification,
    val orderNumber: Int,
    val isBasicDeck: Boolean,
) {
    val currentPlayer = getPlayer(currentPlayerId)
    val exploitationHasToBeUsed = currentPlayer.hasAnyOf(ExploitationBase.Ids)
            && currentPlayer.hasAnyOf(idsForExploitation)
    val isRoundEnd = players.any { it.wonLastRound }
    val standingPlayers = players.filter { !it.isEliminated }
    val winPointCount = 3.coerceAtLeast(8 - players.size)
    val winners = players.filter { it.points == winPointCount }

    fun applyDecision(decision: PlayerDecision): GameState {
        val cardId = if (exploitationHasToBeUsed)
            currentPlayer.getAnyOf(ExploitationBase.Ids).id
        else decision.cardId
        val card = if (currentPlayer.hasCard(cardId))
            CardFactory.create(cardId, true)
        else currentPlayer.card

        val newState = discardFrom(currentPlayer.id, card)
        return card.playSelf(newState, decision)
    }

    fun changeMarksOfCurrent(
        func: (PlayerMarks) -> PlayerMarks,
    ): GameState {
        val newCurrentPlayer = currentPlayer.withMarks(func)
        return withCurrentPlayer(newCurrentPlayer)
    }

    fun withNotification(value: GameNotification): GameState {
        return GameState(currentPlayerId, players, discardPile, stockPile, value, orderNumber, isBasicDeck)
    }

    fun withCurrentPlayer(player: Player): GameState {
        val newPlayers = getPlayersWithout(currentPlayerId)
        newPlayers.add(player)
        return withPlayers(newPlayers)
    }

    fun withCurrentPlayer(playerId: UUID): GameState {
        return GameState(playerId, players, discardPile, stockPile, notification, orderNumber, isBasicDeck)
    }

    fun withPiles(discardPile: Cards, stockPile: Cards): GameState {
        return GameState(currentPlayerId, players, discardPile, stockPile, notification, orderNumber, isBasicDeck)
    }

    fun withPlayers(value: List<Player>): GameState {
        return GameState(currentPlayerId, Players(value), discardPile, stockPile, notification, orderNumber, isBasicDeck)
    }

    fun dealCardToCurrent(): GameState {
        return dealCardTo(currentPlayer.id)
    }

    fun dealCardTo(playerId: UUID): GameState {
        val newPlayers = getPlayersWithout(playerId)
        val card: Card
        var newStock = stockPile
        var newDiscard = discardPile
        if (stockPile.isEmpty) {
            card = discardPile.last
            newDiscard = discardPile.removeOne(card)
        } else {
            card = stockPile.first
            newStock = stockPile.removeOne(card)
        }
        val newPlayer = getPlayer(playerId)
            .addCard(card.toShown())
        newPlayers.add(newPlayer)
        return withPlayers(newPlayers)
            .withPiles(newDiscard, newStock)
    }

    fun discardFrom(playerId: UUID, card: Card): GameState {
        val newPlayers = getPlayersWithout(playerId)
        val newPlayer = getPlayer(playerId)
            .removeCard(card)
        newPlayers.add(newPlayer)
        val newDiscard = discardPile.addAsFirst(card)
        return withPlayers(newPlayers)
            .withPiles(newDiscard, stockPile)
    }

    fun eliminatePlayer(id: UUID): GameState {
        val newPlayers = players.map { if (it.id == id) it.eliminate() else it }
        return withPlayers(newPlayers)
    }

    fun incrementOrderNumber(): GameState {
        return GameState(currentPlayerId, players, discardPile, stockPile, notification, orderNumber + 1, isBasicDeck)
    }

    fun tradeCards(firstPlayerId: UUID, secondPlayerId: UUID): GameState {
        val newPlayers = players
            .filter { it.id != firstPlayerId && it.id != secondPlayerId }
            .toMutableList()
        val firstPlayer = getPlayer(firstPlayerId)
        val secondPlayer = getPlayer(secondPlayerId)
        fun addCard(playerA: Player, playerB: Player) =
            newPlayers.add(playerA.removeCard(playerA.card).addCard(playerB.card))

        addCard(firstPlayer, secondPlayer)
        addCard(secondPlayer, firstPlayer)
        return withPlayers(newPlayers)
    }

    fun withClearedPlayers(): GameState {
        val newPlayers = players.map { it.clearWinMarks() }
        return withPlayers(newPlayers)
    }

    fun winExpansionPlayer(): GameState {
        val expansionPlayers = standingPlayers.filter { it.hadExpansion }
        val expansionPlayerId = if (expansionPlayers.size == 1) expansionPlayers.first().id else Const.Guid.Nil
        return winPlayers({ this.id == expansionPlayerId }, {
            this.winByExpansion()
        }, false)
    }

    fun winLastStandingPlayer(): GameState {
        return winPlayers({ !this.isEliminated }, {
            this.winByElimination()
        })
    }

    fun winPlayersWithBestCard(): GameState {
        val maxPoints = standingPlayers.flatMap { it.cards }
            .maxByOrNull { it.points }!!.points

        return winPlayers({
            this.card.points == maxPoints
        }, { this.winByPoints() })
    }

    private fun winPlayers(
        hasWon: Player.() -> Boolean,
        win: Player.() -> Player,
        setNextPlayer: Boolean = true,
    ): GameState {
        val nextPlayerId = if (setNextPlayer)
            players.first { it.hasWon() }.id
        else currentPlayerId
        val newPlayers = players.map { if (it.hasWon()) it.win() else it.clearEliminate() }
        return create(nextPlayerId, newPlayers, notification, isBasicDeck, orderNumber)
    }

    private fun getPlayer(id: UUID) = players.first { it.id == id }

    private fun getPlayersWithout(playerId: UUID) = players
        .filter { it.id != playerId }
        .toMutableList()

    companion object {
        val idsForExploitation = listOf(Migration.Id, *TradeBase.Ids)

        fun create(
            currentPlayerId: UUID,
            players: List<Player>,
            notification: GameNotification,
            isBasicDeck: Boolean,
            orderNumber: Int = 1,
        ): GameState {
            val discardPile = mutableListOf<Card>()

            val stockPile = CardFactory.getCollection(isBasicDeck).deck
                .toMutableList()
            stockPile.shuffle()
            fun drawCard() = stockPile.removeAt(0)

            if (players.size == 2) {
                for (i in 0..2) {
                    val removed = drawCard()
                    discardPile.add(removed.toShown())
                }
            }
            val removed = drawCard()
            discardPile.add(removed.toHidden())
            val playersWithCards = players.map { it.withCards(Cards(listOf(drawCard().toShown()))) }
            return GameState(
                currentPlayerId,
                Players(playersWithCards),
                Cards(discardPile),
                Cards(stockPile),
                notification,
                orderNumber,
                isBasicDeck,
            ).dealCardToCurrent()
        }
    }
}
