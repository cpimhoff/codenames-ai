import java.util.*

abstract class AIHintAgent : HintAgent {

    val wordHintMap = constants.wordHintMap
    val previousHints = mutableSetOf<String>()

    override fun getHint(board: Board, onRedTeam: Boolean): Hint {
        val candidates = getHintCandidates(board, onRedTeam)

        // build a table with representativeness values from candidate --> card
        val representativenessTable = getRepresentativenessTable(candidates, board)

        // select best option
        val hint = selectBestHint(representativenessTable, board, onRedTeam)
        previousHints.add(hint.word)
        return hint
    }

    // abstract function-ish
    abstract fun selectBestHint(table: Map<Pair<String, String>, Double>, board: Board, onRedTeam: Boolean) : Hint

    internal fun getHintCandidates(board: Board, onRedTeam: Boolean) : Set<String> {
        val teamCards = if (onRedTeam) board.redCardsLeft else board.blueCardsLeft

        // go through each word-hint we've precalculated
        // and only keep the ones which have a listing for one of our cards on the board
        val candidates = wordHintMap.keys
                .filter { pair -> pair.first in teamCards.map { it.word.toUpperCase() } }
                .map { it.second }
                .toMutableSet()

        // don't repeat hints
        candidates.removeAll(this.previousHints)

        return candidates
    }

    private fun getRepresentativenessTable(candidates: Set<String>, board: Board) : Map<Pair<String, String>, Double> {
        val table = HashMap<Pair<String, String>, Double>()

        for (c in candidates) {
            for (w in board.wordsInPlay) {
                val wordHint = Pair(w, c)
                table[wordHint] = representativeness(w, c, board)
            }
        }

        return table
    }

    // Returns the amount this word is envoked by the given hint, compared to the others on the board.
    // If there is no data on the relationship between these two words, the result is zero.
    private fun representativeness(word: String, hint: String, board: Board): Double {
        val wordHint = Pair(word.toUpperCase(), hint.toUpperCase())
        val probabilityOfHintGivenWord = wordHintMap[wordHint] ?: return 0.0

        var denominator = 0.0
        val probabilityOfWordBeingHinted = 1.0 / constants.totalCardCount

        board.cards.map { it.word.toUpperCase() }
                .filter { it != word }
                .forEach {
                    val alternateWordHint = Pair(it, hint)
                    val probabilityOfHintGivenOtherWord = wordHintMap[alternateWordHint] ?: 0.0

                    denominator += probabilityOfHintGivenOtherWord * probabilityOfWordBeingHinted
                }

        return Math.log(probabilityOfHintGivenWord / denominator)
    }
}