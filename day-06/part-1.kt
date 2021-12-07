import java.io.File

data class Fish(var timer: Int) {

    fun spawn(): Fish? {
        timer--

        return when {
            timer >= 0 -> null
            else -> {
                timer = 6
                Fish(8)
            }
        }
    }

}

fun main(args: Array<String>) {
    val inputList = File("input.txt").readLines()
    val fishList = inputList.first().split(",").map { Fish(it.toInt()) }.toMutableList()

    for (i in 0 until 80) {
        fishList.addAll(fishList.mapNotNull { it.spawn() })
    }
    
    println(fishList.count())
}