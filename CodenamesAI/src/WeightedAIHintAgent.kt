import java.util.*

class WeightedAIHintAgent : AIHintAgent() {

	private val teamCardBonus = 100.0
	private val otherTeamCardCost = -100.0
	private val assassinCardCost = -1000.0
	private val bystanderCardCost = -30.0

	private val representativenessThreshold = 0.2

	override fun selectBestHint(table: Map<Pair<String, String>, Double>, board: Board, onRedTeam: Boolean) : Hint {

		// calculate score for each candidate hint
		val candidates = getHintCandidates(board, onRedTeam)
		val scores = HashMap<String, Double>()

		candidates.forEach { scores[it] = score(it, table, board, onRedTeam) }

		// get the maximum scored hint
		val maximumEntry = scores.maxBy { it.value }!!
		val hintWord = maximumEntry.key

		// determine how many good words this activates
		val count = determineCount(hintWord, table, board, onRedTeam)

		return Hint(hintWord, count)
	}

	fun determineCount(hint: String, table: Map<Pair<String, String>, Double>, board: Board, onRedTeam: Boolean) : Int {
		val activatedCards = table.keys
				.filter { it.second == hint }
				.mapNotNull { board.findUnrevealedCard(it.first) }
				.toSet()

		var count = 0
		for (card in activatedCards) {
			val pair = Pair(card.word, hint)
        	val representativeness = table[pair] ?: 0.0

        	val teamCard = (onRedTeam && card.type == CardType.RED) || (!onRedTeam && card.type == CardType.BLUE)

        	if (teamCard && representativeness > representativenessThreshold) {
        		count += 1
    		}
		}

		if (count < 1) {
			return 1	// never give '0' hint
		}
		return count
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
