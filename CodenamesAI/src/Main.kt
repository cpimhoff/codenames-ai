import com.sun.deploy.util.ArgumentParsingUtil
import constants.*

fun main(args: Array<String>) {
    // Init board with random words and assignments to teams
    val board = Board.create()

    // Init hint agents from CLI arguments
    val argMap = createArgMapFromArgs(args)
    if ("-h" in args || "-help" in args) {
        printHelp()
        return
    }
    val blueHinter : HintAgent = getHinterFromArgMap("-blueHinter", argMap)
    val redHinter : HintAgent = getHinterFromArgMap("-redHinter", argMap)

    // Guessers are only implemented for humans:
    val blueGuesser : GuessAgent = HumanGuessAgent()
    val redGuesser : GuessAgent = HumanGuessAgent()

    var isRedTeamTurn = constants.redGoesFirst

    // Main game loop
    while (true) {
        val currentHinter : HintAgent = if (isRedTeamTurn) redHinter else blueHinter
        val currentGuesser : GuessAgent = if (isRedTeamTurn) redGuesser else blueGuesser

        println()
        println(if (isRedTeamTurn) "${ANSI_RED}RED TEAM'S TURN $ANSI_RESET" else "${ANSI_BLUE}BLUE TEAM'S TURN $ANSI_RESET")

        // get hint
        val hint = currentHinter.getHint(board, isRedTeamTurn)
        // guess words for the amount given by hinter
        while (hint.number > 0) {
            // show cards
            board.printGuesserView()
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

    // at the end of the game, reveal the entire board
    board.printFullView()
}

fun  createArgMapFromArgs(args: Array<String>): Map<String, String> {
    if (args.size % 2 != 0) {
        printHelp()
        kotlin.system.exitProcess(0)
    }

    val argMap = mutableMapOf<String, String>()
    for (i in 0..(args.size-1) step 2) {
        argMap.put(args[i], args[i+1])
    }

    return argMap
}

fun getHinterFromArgMap(hinterArgument: String, argMap: Map<String, String>): HintAgent {
    if (argMap[hinterArgument] == "maxRepAI") {
        println("For $hinterArgument, using Max Representative AI hint agent")
        return MaxRepresentativeAIHintAgent()
    } else if (argMap[hinterArgument] == "weightedAI") {
        println("For $hinterArgument, using Weighted AI hint agent")
        return WeightedAIHintAgent()
    } else {
        println("For $hinterArgument, using human agent")
        return HumanHintAgent()
    }
}

fun printHelp() {
    println("Codenames game with AI hinters")
    println("options:")
    println("      -blueHinter maxRepAI | weightedAI | human")
    println("      -redHinter maxRepAI | weightedAI | human")
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
        println("BOOM! Assassin has been activated!")
        if (isRedTeamTurn) {
            println("Blue team wins!")
        } else {
            println("Red team wins!")        
        }
        return true
    }

    return false
}