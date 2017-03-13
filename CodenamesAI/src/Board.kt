import constants.*

import java.util.Random

class Board(val cards : List<WordCard>) {

	// static factory functions
	companion object {
		// create with specified words
		fun create(words: List<String>) : Board {
			if (words.size != constants.totalCardCount) {
				println("warning: incorrect amount of words passed to board constructor")
			}

			val cardsLeft = HashMap<CardType, Int>()
			cardsLeft.put(CardType.ASSASSIN, constants.assassinCardCount)
			cardsLeft.put(CardType.RED, constants.redCardCount)
			cardsLeft.put(CardType.BLUE, constants.blueCardCount)
			cardsLeft.put(CardType.NEUTRAL, constants.bystanderCardCount)

			// initialize a card for each word, assigning the appropriate amount of each type
			val cards = mutableListOf<WordCard>()
			for (word in words) {
				for (type in CardType.values()) {
					val ofTypeLeft = cardsLeft.get(type) ?: 0
					if (ofTypeLeft > 0) {
						// construct card of this type
						val card = WordCard(word, type)
						cards.add(card)

						cardsLeft.put(type, ofTypeLeft - 1)
					}
				}
			}

			// shuffle cards
			val rand = Random()
		    for (i in 0..cards.size - 1) {
		        val randomPosition = rand.nextInt(cards.size)
		        val tmp = cards[i]
		        cards[i] = cards[randomPosition]
		        cards[randomPosition] = tmp
		    }

			// construct and return board object
			return Board(cards)
		}

		// create with randomized words
		fun create() : Board {
			// gather all possible words
			val allWords = constants.allCodenameWords()

			// generate a random set of indices
			val indexSet = mutableSetOf<Int>()
			val rand = Random()
			while (indexSet.size < constants.totalCardCount) {
				val index = rand.nextInt(allWords.size)
				indexSet.add(index)
			}

			// gather up the indexed words into a list
			val words = mutableListOf<String>()
			for (index in indexSet) {
				words.add(allWords[index])
			}

			// create board with those random words
			return Board.create(words)
		}
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
				print("        ")	// empty spot
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

}
