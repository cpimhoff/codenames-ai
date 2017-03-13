import constants.*

fun main(args: Array<String>) {
    // Init board with random words and assignments to teams
    val board = Board.create()
    // Init agents
    val redHinter : HintAgent = HumanHintAgent()        // swap this for AIHintAgent...
    val redGuesser : GuessAgent = HumanGuessAgent()
    val blueHinter : HintAgent = HumanHintAgent()       // swap this for AIHintAgent...
    val blueGuesser : GuessAgent = HumanGuessAgent()

    var isRedTeamTurn = constants.redGoesFirst

    // Main game loop
    while (true) {
        var currentHinter : HintAgent = if(isRedTeamTurn) redHinter else blueHinter
        var currentGuesser : GuessAgent = if(isRedTeamTurn) redGuesser else blueGuesser

        // get hint
        var hint = currentHinter.getHint(board, isRedTeamTurn)
        // guess words for the amount given by hinter
        while(hint.number > 0) {
            val guess = currentGuesser.getGuess(board.wordsInPlay, hint)
            // determine what card this guess applied to
            var guessedCard = board.findUnrevealedCard(guess)
            if (guessedCard == null) {
                println("could not match the guess to a card on the board")
                continue    // try again
            }

            // reveal the card's assignment
            println(guessedCard)
            guessedCard.revealed = true

            // handle success / failure
            val accurateGuess = ((isRedTeamTurn && guessedCard.type == CardType.RED) || (!isRedTeamTurn && guessedCard.type == CardType.BLUE))

            if (!accurateGuess) {
                break   // no more guess attempts
            }
        }

        // check for win condition

    }

    // Main loop:
    //  1. Determine whose turn it is
    //  2. Give board to hinter of that team, receive Hint (String, Int)
    //  3. while (guess is in range(number of words), no incorrect guesses) {
    //      4. Get guess from guesser
    //      5. Check guess, update board
    //  }
    //  6. Check for win

}