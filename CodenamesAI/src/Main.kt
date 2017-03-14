import constants.*

fun main(args: Array<String>) {
    // Init board with random words and assignments to teams
    val board = Board.create()
    // Init agents
    val redHinter : HintAgent = MaxRepresentativeAIHintAgent()
    val redGuesser : GuessAgent = HumanGuessAgent()
    val blueHinter : HintAgent = WeightedAIHintAgent()
    val blueGuesser : GuessAgent = HumanGuessAgent()

    var isRedTeamTurn = constants.redGoesFirst

    // Main game loop
    while (true) {
        val currentHinter : HintAgent = if (isRedTeamTurn) redHinter else blueHinter
        val currentGuesser : GuessAgent = if (isRedTeamTurn) redGuesser else blueGuesser

        println()
        println(if (isRedTeamTurn) "${ANSI_RED}RED TEAM'S TURN $ANSI_RESET" else "${ANSI_BLUE}BLUE TEAM'S TURN $ANSI_RESET")
        println()

        // get hint
        val hint = currentHinter.getHint(board, isRedTeamTurn)
        // guess words for the amount given by hinter
        while (hint.number > 0) {
            val guess = currentGuesser.getGuess(board.wordsInPlay, hint)
            // determine what card this guess applied to
            val guessedCard = board.findUnrevealedCard(guess)
            if (guessedCard == null) {
                println("could not match guess to a card on the board")
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

            // decrement hint
            hint.decrement()
        }

        // check game end state
        if (isGameOver(board, isRedTeamTurn)) {
            break
        }

        // progress to next turn
        isRedTeamTurn = !isRedTeamTurn
    }
}

fun isGameOver(board: Board, isRedTeamTurn: Boolean) : Boolean {
    if (board.redCardsLeft.isEmpty()) {
        // red win
        println("Red team has activated all their cards!")
        println("Red team wins!")
        return true
    } else if (board.blueCardsLeft.isEmpty()) {
        // blue win
        println("Blue team has activated all their cards!")
        println("Blue team wins!")
        return true
    } else if (board.assassinCardsLeft.isEmpty()) {
        // whoever went last loss
        println("BOOM! Assassin has been actived!")
        if (isRedTeamTurn) {
            println("Blue team wins!")
        } else {
            println("Red team wins!")        
        }
        return true
    }

    return false
}