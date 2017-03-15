import constants.*
import java.util.*

class Board(val cards : List<WordCard>) {

	// static factory functions
	companion object {
		// create with specified words
		fun create(words: List<String>) : Board {
			if (words.size != constants.totalCardCount) {
				println("warning: incorrect amount of words passed to board constructor")
			}

			// initialize a card for each word, assigning the appropriate amount of each type
            val cards = mutableListOf<WordCard>()

            // Shuffle list first:
            Collections.shuffle(words)

            // Now assign roles in order
            for (i in 0..constants.totalCardCount) {
                if (i < redCardCount) {
                    cards.add(WordCard(words[i], CardType.RED))
                } else if (i < redCardCount + blueCardCount) {
                    cards.add(WordCard(words[i], CardType.BLUE))
                } else if (i < redCardCount + blueCardCount + bystanderCardCount) {
                    cards.add(WordCard(words[i], CardType.NEUTRAL))
                } else if (i < redCardCount + blueCardCount + bystanderCardCount + assassinCardCount) {
                    cards.add(WordCard(words[i], CardType.ASSASSIN))
                }
            }

            // Now shuffle list of cards
            Collections.shuffle(cards)

            // construct and return board object
			return Board(cards)
		}

		// create with randomized words
		fun create() : Board {
			// gather all possible words
			val allWords = constants.allCodenameWords

			// generate a random set of indices
			val indexSet = mutableSetOf<Int>()
			val rand = Random()
			while (indexSet.size < constants.totalCardCount) {
				val index = rand.nextInt(allWords.size)
				indexSet.add(index)
			}

			// gather up the indexed words into a list
            val words = indexSet.map { allWords[it] }

            // create board with those random words
			return Board.create(words)
		}
	}

	fun findUnrevealedCard(guess: String) : WordCard? {
		for (card in this.cards) {
            if (card.word.toLowerCase() == guess.toLowerCase() && !card.revealed) {
                return card
            }
        }
        return null   // not found
	}

	// description of the board with all information
	fun printHinterView() {
		val rows = 4
		val columns = cards.size / rows

		println()
		for (i in 0..cards.size - 1) {
			val card = cards[i]
			if (card.revealed) {
				print("        ")	// empty spot
			} else {
				print(card.toString() + "  ")
			}
			if (i != 0 && i % columns == 0) {
				println()	// newline
			}
		}
		println()
	}

	// description of the board with only guesser-revealed information
	fun printGuesserView() {
		val rows = 4
		val columns = cards.size / rows

		println()
		for (i in 0..cards.size - 1) {
			val card = cards[i]
			
			if (card.revealed) {
				print(card.toString() + "  ")
			} else {
				print(card.toHiddenString() + "  ")
			}

			if (i != 0 && i % columns == 0) {
				println()	// newline
			}
		}
		println()
	}

	val wordsInPlay : List<String>
		get() {
			val result = mutableListOf<String>()
			for (card in this.cards) {
				if (!card.revealed) {
					result.add(card.word)
				}
			}
			return result
		}

	val blueCardsLeft : List<WordCard>
		get() {
			val result = mutableListOf<WordCard>()
			for (card in this.cards) {
				if (card.type == CardType.BLUE && !card.revealed) {
					result.add(card)
				}
			}
			return result
		}

	val redCardsLeft : List<WordCard>
		get() {
			val result = mutableListOf<WordCard>()
			for (card in this.cards) {
				if (card.type == CardType.RED && !card.revealed) {
					result.add(card)
				}
			}
			return result
		}

	val neutralCardsLeft : List<WordCard>
		get() {
			val result = mutableListOf<WordCard>()
			for (card in this.cards) {
				if (card.type == CardType.NEUTRAL && !card.revealed) {
					result.add(card)
				}
			}
			return result
		}

	val assassinCardsLeft : List<WordCard>
		get() {
			val result = mutableListOf<WordCard>()
			for (card in this.cards) {
				if (card.type == CardType.ASSASSIN && !card.revealed) {
					result.add(card)
				}
			}
			return result
		}

}