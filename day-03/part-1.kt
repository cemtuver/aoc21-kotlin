import java.io.File

fun main() {
    val inputList = File("input.txt").readLines()
    var readingMap = mutableMapOf<Int, Int>()

    inputList.forEach { input ->
        input.forEachIndexed { index, char -> 
            val currentValue = readingMap[index] ?: 0
            val newValue = when (char) {
                '1' -> currentValue + 1
                else -> currentValue - 1
            }

            readingMap.put(index, newValue)
        }
    }

    val gammaIntList = readingMap.values.map {
        when {
            it > 0 -> 1
            else -> 0
        }
    }

    val epsilonIntList = readingMap.values.map {
        when {
            it > 0 -> 0
            else -> 1
        }
    }

    val gamma = Integer.parseInt(gammaIntList.joinToString(""), 2)
    val epsilon = Integer.parseInt(epsilonIntList.joinToString(""), 2)

    println(gamma * epsilon)
}