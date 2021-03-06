import java.util.*

class WeightedAIHintAgent : AIHintAgent() {

	private val teamCardBonus = 100.0
	private val otherTeamCardCost = -100.0
	private val assassinCardCost = -1000.0
	private val bystanderCardCost = -50.0

	/**
     * This agent will select a hint with the highest representativeness to team word(s) on the board
     * taking into account the risk of giving a hint which represents the other cards.
     */
	override fun selectBestHint(table: Map<Pair<String, String>, Double>, board: Board, onRedTeam: Boolean) : Hint {
		// get new candidates
		val candidates = getHintCandidates(board, onRedTeam)

		// determine score for each
		val scores = HashMap<String, Double>()
		candidates.forEach { scores[it] = score(it, table, board, onRedTeam) }

		// get the maximum scored hint
		val maximumEntry = scores.maxBy { it.value }!!
		val hintWord = maximumEntry.key

		// determine how many good words this activates
		val count = determineCount(hintWord, table, board, onRedTeam)

		return Hint(hintWord, count)
	}

	// give a numeric score for the proposed hint for use when comparing to other hint options
	fun score(hint: String, table: Map<Pair<String, String>, Double>, board: Board, onRedTeam: Boolean) : Double {
		val activatedCards = table.keys
				.filter { it.second == hint }
				.mapNotNull { board.findUnrevealedCard(it.first) }
				.toSet()

		var value = 0.0
        for (card in activatedCards) {
        	val pair = Pair(card.word, hint)
        	val representativeness = table[pair] ?: 0.0

        	when (card.type) {
				CardType.BLUE -> {
					if (!onRedTeam) {
						value += teamCardBonus * representativeness
					} else {
						value += otherTeamCardCost * representativeness
					}
				}
				CardType.RED -> {
					if (onRedTeam) {
						value += teamCardBonus * representativeness
					} else {
						value += otherTeamCardCost * representativeness
					}
				}
				CardType.NEUTRAL -> value += bystanderCardCost * representativeness
				CardType.ASSASSIN -> value += assassinCardCost * representativeness
        	}
        }

        return value
	}

}
