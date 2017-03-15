class MaxRepresentativeAIHintAgent : AIHintAgent() {

    /**
     * This agent will select a hint with the highest representativeness
     *  and give that hint for only one word.
     */
    override fun selectBestHint(table: Map<Pair<String, String>, Double>, board: Board, onRedTeam: Boolean): Hint {
        val teamCards = if (onRedTeam) board.redCardsLeft else board.blueCardsLeft
        val maxValueKey = table.filter { it.key.first.toUpperCase() in teamCards.map{ it.word.toUpperCase() } }.maxBy { it.value }

        val hintWordWithMaxRepresentativeness = maxValueKey?.key?.second
        val count = determineCount(hintWordWithMaxRepresentativeness, table, board, onRedTeam)

        return Hint(hintWordWithMaxRepresentativeness!!, count)
    }
}