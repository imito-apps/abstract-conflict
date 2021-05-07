package imito.ac.entities.player

class PlayerMarks(
    val isEliminated: Boolean,
    val hadDefence: Boolean,
    val hadExpansion: Boolean,
    val wonByExpansion: Boolean,
    val wonByElimination: Boolean,
    val wonByPoints: Boolean,
) {
    companion object

    val wonLastRound = wonByPoints || wonByElimination || wonByExpansion

    fun clearWin() = PlayerMarks(isEliminated, false, false, false, false, false)

    fun withExpansion() = PlayerMarks(isEliminated, hadDefence, true, wonByExpansion, wonByElimination, wonByPoints)

    fun withDefence(value: Boolean) =
        PlayerMarks(isEliminated, value, hadExpansion, wonByExpansion, wonByElimination, wonByPoints)

    fun withEliminated(value: Boolean) =
        PlayerMarks(value, hadDefence, hadExpansion, wonByExpansion, wonByElimination, wonByPoints)
}
