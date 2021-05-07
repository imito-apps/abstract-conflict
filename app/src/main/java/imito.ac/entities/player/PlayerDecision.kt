package imito.ac.entities.player

import java.lang.*
import java.util.*

class PlayerDecision(
    val cardId: Int,
    val value1: String = "",
    val value2: String = "",
) {
    constructor(cardId: Int, value1: Any, value2: Any = "")
            : this(cardId, value1.toString(), value2.toString())

    companion object

    fun getTargetPlayerId(): UUID? {
        return try {
            UUID.fromString(value1)
        } catch (exc: Exception) {
            null
        }
    }

    val targetNumber get() = value2.toIntOrNull()
}
