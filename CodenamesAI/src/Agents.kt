interface HintAgent {

	fun getHint(board: Board, onRedTeam: Boolean) : Hint

}

interface GuessAgent {

	fun getGuess(wordsInPlay: List<String>, hint: Hint) : String

}