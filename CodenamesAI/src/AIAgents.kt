import java.util.*

abstract class AIHintAgent : HintAgent {

    val wordHintMap = constants.wordHintMap

    override fun getHint(board: Board, onRedTeam: Boolean): Hint {
        val candidates = getHintCandidates(board, onRedTeam)

        // build a table with representativeness values from candidate --> card
        val representativenessTable = getRepresentativenessTable(candidates, board)

        // select best option
        return selectBestHint(representativenessTable)
    }

    // abstract function-ish
    abstract fun selectBestHint(table: Map<Pair<String, String>, Double>) : Hint

    private fun getHintCandidates(board: Board, onRedTeam: Boolean) : Set<String> {
        val teamCards = if (onRedTeam) board.redCardsLeft else board.blueCardsLeft
        val candidates = mutableSetOf<String>()

        // go through each word-hint we've precalculated
        // and only keep the ones which have a listing for one of our cards on the board
        for (pair in wordHintMap.keys) {
            if (pair.first in teamCards.map { it.word.toUpperCase() }) {
                candidates.add(pair.second)
            }
        }

        return candidates
    }

    private fun getRepresentativenessTable(candidates: Set<String>, board: Board) : Map<Pair<String, String>, Double> {
        val table = HashMap<Pair<String, String>, Double>()

        for (c in candidates) {
            for (w in board.wordsInPlay) {
                val wordHint = Pair<String, String>(w, c)
                table[wordHint] = representativeness(w, c, board)
            }
        }

        return table
    }

    private fun representativeness(word: String, hint: String, board: Board): Double {
        val wordHint = Pair<String, String>(word, hint)
        val probabilityOfHintGivenWord = wordHintMap[wordHint] ?: 0.0

        var denominator = 0.0
        val probabilityOfWordBeingHinted = 1.0 / constants.totalCardCount

        board.cards.map { it.word }
                .filter { it != word }
                .forEach {
                    val alternateWordHint = Pair<String, String>(it, hint)
                    val probabilityOfHintGivenOtherWord = wordHintMap[alternateWordHint] ?: 0.0

                    denominator += probabilityOfHintGivenOtherWord * probabilityOfWordBeingHinted
                }

        return probabilityOfHintGivenWord / denominator
    }
}