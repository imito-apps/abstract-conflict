package imito.ac.entities.player

import imito.core.entities.*
import imito.ac.entities.cards.*
import java.util.*

class Player(
    id: UUID,
    name: String,
    val cards: Cards,
    val points: Int,
    val marks: PlayerMarks,
) : UserBase(id, name) {
    companion object

    val canBeTargeted = !marks.isEliminated && !marks.hadDefence
    val card = cards.first
    val isEliminated = marks.isEliminated
    val hadDefence = marks.hadDefence
    val hadExpansion = marks.hadExpansion
    val wonByExpansion = marks.wonByExpansion
    val wonByElimination = marks.wonByElimination
    val wonByPoints = marks.wonByPoints
    val wonLastRound = marks.wonLastRound

    fun clearWinMarks(): Player {
        return Player(id, name, cards, points, marks.clearWin())
    }

    fun clearEliminate(): Player {
        val newMarks = marks.withEliminated(false)
        return Player(id, name, cards, points, newMarks)
    }

    fun eliminate(): Player {
        val newMarks = marks.clearWin()
            .withEliminated(true)
        return Player(id, name, cards, points, newMarks)
    }

    fun withCards(value: Cards): Player {
        return Player(id, name, value, points, marks)
    }

    fun withMarks(
        func: (PlayerMarks) -> PlayerMarks,
    ): Player {
        val newMarks = func(marks)
        return Player(id, name, cards, points, newMarks)
    }

    fun getAnyOf(cardIds: Iterable<Int>) = cards.getAnyOf(cardIds)

    fun hasAnyOf(cardIds: Iterable<Int>) = cards.hasAnyOf(cardIds)

    fun hasCard(cardId: Int) = cards.hasId(cardId)

    fun addCard(card: Card): Player {
        return withCards(cards.addAsFirst(card))
    }

    fun removeCard(card: Card): Player {
        return withCards(cards.removeOne(card))
    }

    fun winByExpansion(): Player {
        return win(true, wonByElimination, wonByPoints)
    }

    fun winByElimination(): Player {
        return win(wonByExpansion, true, wonByPoints)
    }

    fun winByPoints(): Player {
        return win(wonByExpansion, wonByElimination, true)
    }

    private fun win(byExpansion: Boolean, byElimination: Boolean, byPoints: Boolean): Player {
        val newMarks = PlayerMarks(isEliminated, hadDefence, hadExpansion, byExpansion, byElimination, byPoints)
        return Player(id, name, cards, points + 1, newMarks)
    }
}
