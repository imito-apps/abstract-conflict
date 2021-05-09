package imito.ac.models

import android.content.res.*
import imito.core.collections.*
import imito.ac.entities.player.*
import java.util.*

class PlayerModels(
    value: List<PlayerModel>,
) : ChainableList<PlayerModel, UUID, PlayerModels>(value) {
    override fun createSelf(value: List<PlayerModel>) = PlayerModels(value)

    val canAnyBeTargeted = value.any { it.canBeTargeted }

    companion object {
        fun from(players: Iterable<Player>, currentPlayerId: UUID, resources: Resources): PlayerModels {
            return PlayerModels(players.map { PlayerModel(it, currentPlayerId, resources) })
        }
    }
}
