package imito.ac.serializers

import imito.core.*
import imito.ac.entities.cards.*
import imito.ac.entities.player.*
import java.util.*

private const val Separator = '\u0007'

fun Player.serialize() = "$id$name" +
        "$Separator${cards.serialize()}" +
        "$Separator$points" +
        "$Separator${marks.serialize()}"

fun Player.Companion.deserialize(text: String): Player {
    val id = UUID.fromString(text.substring(0, Const.Guid.Length))
    val properties = text
        .substring(Const.Guid.Length)
        .split(Separator)
    val name = properties[0]
    val cards = Cards.deserialize(properties[1])
    val points = properties[2].toInt()
    val marks = PlayerMarks.deserialize(properties[3])
    return Player(id, name, cards, points, marks)
}
