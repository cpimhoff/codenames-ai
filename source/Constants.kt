package codenames.const

import java.util.ArrayList
import java.io.File

//
// 	Constants to approxmimate the official game
//	http://czechgames.com/files/rules/codenames-rules-en.pdf
//

val assassinCardCount = 1
val redCardCount = 9
val blueCardCount = 8
val bystanderCardCount = 7

val totalCards = assassinCardCount + redCardCount + blueCardCount + bystanderCardCount

val redGoesFirst = true

fun allCodenameWords() : ArrayList<String> {
	val allWords = File("CodenameWords.txt")
	val lines = allWords.readLines()

	val result = ArrayList<String>()
	lines.forEach {
		if (it != "") {
			result.add(it)
		}
	}
	return result
}