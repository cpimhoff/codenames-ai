package constants

import java.io.File
import java.util.*

//
// 	Constants to approximate the official game
//	http://czechgames.com/files/rules/codenames-rules-en.pdf
//

val assassinCardCount = 1
val redCardCount = 9
val blueCardCount = 8
val bystanderCardCount = 7

val totalCardCount = assassinCardCount + redCardCount + blueCardCount + bystanderCardCount

val redGoesFirst = true

fun allCodenameWords() : List<String> {
	val allWords = File("CodenameWords.txt")
	val lines = allWords.readLines()

	val result = mutableListOf<String>()
	lines.forEach {
		if (it != "") {
			result.add(it)
		}
	}

	return result
}

data class WordHintPair(val word: String, val hint: String, val hintProbGivenWord: Double)

val wordHintMap: Map<String, MutableList<WordHintPair>>
    get() {
        val wordHintMap = HashMap<String, MutableList<WordHintPair>>()

        val wordAssociationsData = File("WordAssocData/word.associations.csv")
        val rows = wordAssociationsData.readLines().toMutableList()
        rows.removeAt(0) // delete headers

        rows.forEach {
            val columns = it.split(",")

            // Remove quotes from words
            val word = columns[1].removeSurrounding(prefix = "\"", suffix = "\"")
            val hint = columns[2].removeSurrounding(prefix = "\"", suffix = "\"")

            val hintProbabilityGivenWord = columns[7].toDouble()

            val wordHintPair = WordHintPair(word, hint, hintProbabilityGivenWord)

            if (word !in wordHintMap) {
                wordHintMap.put(word, mutableListOf(wordHintPair))
            } else {
                val wordHintPairs: MutableList<WordHintPair> = wordHintMap[word]!!
                wordHintPairs.add(wordHintPair)

                wordHintMap.put(word, wordHintPairs)
            }
        }

        return wordHintMap
    }