package imito.ac.entities.player

import imito.core.collections.*
import java.util.*

class Players(
    value: List<Player>,
) : ChainableList<Player, UUID, Players>(value) {
    override fun createSelf(value: List<Player>) = Players(value)

    companion object
}
