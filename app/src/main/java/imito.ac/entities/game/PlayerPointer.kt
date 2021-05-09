package imito.ac.entities.game

import java.util.*

class PlayerPointer(
    private val playerIds: List<UUID>,
    private val currentIdIndex: Int = 0,
) {
    val currentId = playerIds[currentIdIndex]

    fun nextId(): PlayerPointer {
        var newIdIndex = currentIdIndex + 1
        if (newIdIndex >= playerIds.size) newIdIndex = 0

        return PlayerPointer(playerIds, newIdIndex)
    }

    fun withCurrentId(id: UUID): PlayerPointer {
        val newIdIndex = playerIds.indexOf(id)
        return PlayerPointer(playerIds, newIdIndex)
    }
}
