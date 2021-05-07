package imito.ac.models

import android.content.res.*
import imito.core.entities.*
import imito.ac.entities.player.*
import imito.ac.models.cards.*
import java.util.*

class PlayerModel(
    entity: Player,
    currentPlayerId: UUID,
    resources: Resources,
) : UserBase(entity.id, entity.name) {
    val canBeTargeted = entity.canBeTargeted
    val cards = CardModels.from(entity.cards, resources)
    val hadDefence = entity.hadDefence
    val isCurrent = id == currentPlayerId
    val isEliminated = entity.isEliminated
    val points = entity.points
    val wonByElimination = entity.wonByElimination
    val wonByPoints = entity.wonByPoints
    val wonByExpansion = entity.wonByExpansion
}
