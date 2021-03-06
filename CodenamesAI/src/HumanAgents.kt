class HumanGuessAgent : GuessAgent {

	override fun getGuess(wordsInPlay: List<String>, hint: Hint) : String {
		// prompt
		println("Give guess for ${hint.number} card(s) relating to hint ${hint.word}: ")

		// get input or exit if input is null
		val guess = readLine() ?: kotlin.system.exitProcess(0)
		return guess
	}
}

class HumanHintAgent : HintAgent {

	override fun getHint(board: Board, onRedTeam: Boolean) : Hint {
		// print information to screen (as confidentially as a command line application allows)
		board.printHinterView()
		println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n")	// add some space, so as to hide the above from human guesser eyes!
		println("-- hinter view above, scroll up to view --")

		// prompt
		val teamColor = if (onRedTeam) "Red" else "Blue"
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
