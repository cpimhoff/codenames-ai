class HumanGuessAgent : GuessAgent {

	override fun getGuess(wordsInPlay: List<String>, hint: Hint) : String {
		println(wordsInPlay)

		// prompt
		println("Give guess for ${hint.number} card(s) relating to hint ${hint.word}: ")

		// get input or exit if input is null
		val guess = readLine() ?: kotlin.system.exitProcess(0)

		return guess
	}
}

class HumanHintAgent : HintAgent {

	override fun getHint(board: Board, onRedTeam: Boolean) : Hint {
		// print information to screen
		board.printHinterView()

		val teamColor = if (onRedTeam) "Red" else "Blue"

		// prompt
		println("Give Hint for $teamColor Team:")

		// loop until a successfully parsed hint
		while (true) {
			// Read input, or exit if input is null:
			val input = readLine() ?: kotlin.system.exitProcess(0)

			try {
				val tokens = input.split(" ")
				val word = tokens[0]
				val number = tokens[1].toInt()

				val hint = Hint(word, number)
				return hint

			} catch (e: Exception) {
				println("FORMAT: word integer")
				continue	// try again
			}


		}
	}
}
