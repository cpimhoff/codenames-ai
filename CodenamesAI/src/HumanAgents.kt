class HumanGuessAgent : GuessAgent {

	override fun getGuess(wordsInPlay: List<String>, hint: Hint) : String {
		println(wordsInPlay)

		// prompt
		println("Give guess for hint: ")

		// get input
		val guess = readLine()
		if (guess == null) {
			kotlin.system.exitProcess(0)	// end of feed, exit
		}

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
			val input = readLine()
			if (input == null) {
				kotlin.system.exitProcess(0)	// end of feed, exit
			}

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
