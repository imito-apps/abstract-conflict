package imito.ac.serializers

import imito.core.collections.*
import imito.ac.entities.player.*

private const val Separator = "\u0006"

fun Players.serialize() = this.joinToString(Separator) { it.serialize() }

fun Players.Companion.deserialize(text: String): Players {
    val list = ChainableListBase.deserialize(Separator, text) {
        Player.deserialize(it)
    }
    return Players(list)
}
