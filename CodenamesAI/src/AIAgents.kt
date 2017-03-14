import constants.WordHintPair
import java.util.*

class AIHintAgent : HintAgent {

    val wordHintMap = constants.wordHintMap

    override fun getHint(board: Board, onRedTeam: Boolean): Hint {
        val allWordHintPairsForHintCandidates = getWordHintCandidates(board, onRedTeam)

        // Find most representative WordHint pair
        var mostRepresentativePair = allWordHintPairsForHintCandidates.first()
        var maxRepresentativeness = representativeness(mostRepresentativePair, board)

        // Todo: Keep list of hints with equal representativeness (highest r) and pick randomly from that

        allWordHintPairsForHintCandidates.forEach {
            val r = representativeness(it, board)
            if (r > maxRepresentativeness) {
                maxRepresentativeness = r
                mostRepresentativePair = it
            }
        }

        // TODO: Remove this. Used only for debugging
        println("Considering hints: ${allWordHintPairsForHintCandidates.map{it.hint}}")

        return Hint(mostRepresentativePair.hint, 1)
    }

    private fun getWordHintCandidates(board: Board, onRedTeam: Boolean): Set<WordHintPair> {
        val teamCards = if (onRedTeam) board.redCardsLeft else board.blueCardsLeft
        val wordHintPairs = emptySet<WordHintPair>().toMutableSet()

        // Go through the words from each card and add their hints to the wordHintPairs list:
        teamCards.map { wordHintMap[it.word.toUpperCase()] }
                .forEach { hints -> hints?.forEach { wordHintPairs.add(it) } }

        return wordHintPairs
    }

    // Get all the WordHintPairs for all words on the board and all candidate hints
    private fun getAllWordCandidatePairsFromBoard(board: Board, onRedTeam: Boolean): Set<WordHintPair> {
        val candidatePairs = getWordHintCandidates(board, onRedTeam)
        val wordHintPairsForCandidates = emptySet<WordHintPair>().toMutableSet()

        board.cards.forEach {
            val wordsForCandidate = wordHintMap[it.word.toUpperCase()]?.filter {
                it.hint in candidatePairs.map{it.hint}
            } ?: emptyList()

            if (wordsForCandidate.isNotEmpty()) {
                wordHintPairsForCandidates.addAll(wordsForCandidate)
            }
        }

        return wordHintPairsForCandidates
    }

    private fun representativeness(wordHintPair: WordHintPair, board: Board): Double {
        val probabilityOfHintGivenWord = wordHintPair.hintProbGivenWord
        val probabilityOfWordBeingHinted = 1.0 / constants.totalCardCount

        var denominator = 0.0

        board.cards.map { it.word }
                .filter { it != wordHintPair.word }
                .forEach {
                    val hints = wordHintMap[it.toUpperCase()]?.filter { it.hint.toUpperCase() == wordHintPair.hint } ?: emptyList()

                    if (hints.isNotEmpty()) {
                        val probabilityOfHintGivenOtherWord = hints.first().hintProbGivenWord
                        denominator += probabilityOfHintGivenOtherWord * probabilityOfWordBeingHinted
                    }

                }

        return probabilityOfHintGivenWord / denominator
    }
}