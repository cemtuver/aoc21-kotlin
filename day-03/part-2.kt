import java.io.File

fun main(args: Array<String>) {
    val inputList = File("input.txt").readLines()
    val mostCommonLines = mutableListOf<String>()
    val leastCommonLines = mutableListOf<String>()

    inputList.forEach {
        mostCommonLines.add(it)
        leastCommonLines.add(it)
    }

    for (i in 0 until inputList.first().length) {
        val mostCommonValue = getMostCommonValue(mostCommonLines, i)
        val leastCommonValue = getLeastCommonValue(leastCommonLines, i)

        inputList.forEach { input ->
            val value = if (input[i] == '1') 1 else 0

            if (value != mostCommonValue && mostCommonLines.size > 1) {
                mostCommonLines.remove(input)
            }

            if (value != leastCommonValue && leastCommonLines.size > 1) {
                leastCommonLines.remove(input)
            }
        }
    }

    val o2 = Integer.parseInt(mostCommonLines.first(), 2)
    val co2 = Integer.parseInt(leastCommonLines.first(), 2)

    println(o2 * co2)
}

fun getMostCommonValue(list: List<String>, index: Int): Int {
    var sum = 0

    list.forEach { line ->
        sum = when (line[index]) {
            '1' -> sum.plus(1)
            else -> sum.minus(1)
        }
    }

    return when {
        sum >= 0 -> 1
        else -> 0
    }
}


fun getLeastCommonValue(list: List<String>, index: Int): Int {
    var sum = 0

    list.forEach { line ->
        sum = when (line[index]) {
            '1' -> sum.plus(1)
            else -> sum.minus(1)
        }
    }

    return when {
        sum >= 0 -> 0
        else -> 1
    }
}