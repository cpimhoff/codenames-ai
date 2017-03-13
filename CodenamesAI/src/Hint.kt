data class Hint(val word: String, var number: Int) {
	override fun toString() : String {
		return word + ", " + number
	}

	fun decrement() {
		this.number -= 1
	}
}

