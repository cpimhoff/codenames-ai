import java.util.*

class WeightedAIHintAgent : AIHintAgent() {

	private val teamCardBonus = 100.0
	private val otherTeamCardCost = -100.0
	private val assassinCardCost = -1000.0
	private val bystanderCardCost = -20.0

	override fun selectBestHint(table: Map<Pair<String, String>, Double>, board: Board, onRedTeam: Boolean) : Hint {

		// calculate score for each candidate hint
		val candidates = getHintCandidates(board, onRedTeam)
		val scores = HashMap<String, Double>()

		candidates.forEach { scores[it] = score(it, table, board, onRedTeam) }

		// return the maximum
		val maximumEntry = scores.maxBy { it.value }!!
		val hintWord = maximumEntry.key
		return Hint(hintWord, 1)
	}

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
