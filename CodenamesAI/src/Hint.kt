data class Hint(val word: String, val number: Int) {
	override fun toString() : String {
		return word + ", " + number
	}

	fun decremented() : Hint? {
		if (this.number - 1 <= 0) {
			return null
		} else {
			return Hint(this.word, number - 1)
		}
	}
}

