enum class CardType {
	BLUE, RED, NEUTRAL, ASSASSIN
}

data class WordCard(val word: String, val type: CardType, var guessed: Boolean = false)