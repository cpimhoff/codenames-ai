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

val allCodenameWords : List<String> 
    get() {
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

val wordHintMap: Map<Pair<String, String>, Double>
    get() {
        val wordHintMap = HashMap<Pair<String, String>, Double>()

        val wordAssociationsData = File("WordAssocData/word.associations.csv")
        val rows = wordAssociationsData.readLines().toMutableList()
        rows.removeAt(0) // delete headers

        rows.forEach {
            val columns = it.split(",")

            // Remove quotes from words
            val word = columns[1].removeSurrounding(prefix = "\"", suffix = "\"")
            val hint = columns[2].removeSurrounding(prefix = "\"", suffix = "\"")

            val hintProbabilityGivenWord = columns[7].toDouble()

            val wordHint = Pair<String, String>(word, hint)
            wordHintMap.put(wordHint, hintProbabilityGivenWord)
        }

        return wordHintMap
    }