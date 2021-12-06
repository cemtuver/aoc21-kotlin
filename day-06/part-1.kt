import java.io.File

const val SIMULATION_DAY = 80
const val FISH_SPAWN_INTERVAL = 6
const val FISH_SPAWN_INTERVAL_DELAY = 2

data class Fish(var timer: Int) {

    fun onDayPassed(): Fish? {
        timer--

        return when {
            timer >= 0 -> null
            else -> {
                timer = FISH_SPAWN_INTERVAL
                Fish(FISH_SPAWN_INTERVAL + FISH_SPAWN_INTERVAL_DELAY)
            }
        }
    }

}

fun main(args: Array<String>) {
    val inputList = File("input.txt").readLines()
    val fishList = inputList.first().split(",").map { Fish(it.toInt()) }.toMutableList()

    for (i in 0 until SIMULATION_DAY) {
        fishList.addAll(fishList.mapNotNull { it.onDayPassed() })
    }
    
    println(fishList.count())
}