package imito.ac.models

import android.content.res.*
import imito.ac.entities.game.*
import imito.ac.models.cards.*

class GameStateModel(
    private val entity: GameState,
    private val resources: Resources,
) {
    val currentPlayerId = entity.currentPlayerId
    val exploitationHasToBeUsed = entity.exploitationHasToBeUsed
    val notification = entity.notification
    val players = PlayerModels.from(entity.players, currentPlayerId, resources)
    val standingPlayers = PlayerModels.from(entity.standingPlayers, currentPlayerId, resources)
    val stockCount = entity.stockPile.count()
    val stockPile get() = CardModels.from(entity.stockPile, resources)
    val discardPile = CardModels.from(entity.discardPile, resources)
    val isRoundEnd = entity.isRoundEnd
    val winners = PlayerModels.from(entity.winners, currentPlayerId, resources)
    val winPointCount = entity.winPointCount
}
