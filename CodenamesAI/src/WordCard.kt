enum class CardType {
	BLUE, RED, NEUTRAL, ASSASSIN
}

val ANSI_RESET = "\u001B[0m"
val ANSI_RED = "\u001B[31m"
val ANSI_BLUE = "\u001B[34m"
val ANSI_YELLOW = "\u001B[33m"
val ANSI_WHITE = "\u001B[37m"
val ANSI_BLACK_BACKGROUND = "\u001B[40m"

data class WordCard(val word: String, val type: CardType, var revealed: Boolean = false) {

	val casedWord : String
		get() { return this.word.toLowerCase().capitalize() }

	override fun toString() : String {
		when(this.type) {
			CardType.BLUE -> return ANSI_BLUE + casedWord + ANSI_RESET
			CardType.RED -> return ANSI_RED + casedWord + ANSI_RESET
			CardType.NEUTRAL -> return ANSI_YELLOW + casedWord + ANSI_RESET
			CardType.ASSASSIN -> return ANSI_WHITE + ANSI_BLACK_BACKGROUND + casedWord + ANSI_RESET
		}
	}

	fun toHiddenString() : String {
		if (this.revealed) {
			return this.toString()
		} else {
			return this.casedWord
		}
	}

}