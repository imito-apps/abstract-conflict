package imito.ac.serializers

import imito.ac.entities.player.*

private const val Separator = '\u0010'

fun PlayerMarks.serialize() = "$isEliminated" +
        "$Separator$hadDefence" +
        "$Separator$wonByExpansion" +
        "$Separator$wonByElimination" +
        "$Separator$wonByPoints"

fun PlayerMarks.Companion.deserialize(text: String): PlayerMarks {
    val properties = text.split(Separator)
    fun getAt(index: Int) = properties[index].toBoolean()

    val isEliminated = getAt(0)
    val hadDefence = getAt(1)
    val wonByExpansion = getAt(2)
    val wonByElimination = getAt(3)
    val wonByPoints = getAt(4)
    return PlayerMarks(isEliminated, hadDefence, false, wonByExpansion, wonByElimination, wonByPoints)
}
