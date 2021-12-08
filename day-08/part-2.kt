import java.io.File
import kotlin.math.absoluteValue

fun findSegmentConfig(
    digits: List<List<String>>, 
    size: Int, 
    predicate: (List<String>) -> Boolean = { true }
): List<String> {
    return digits.first { it.size == size && predicate(it) }
}

fun main(args: Array<String>) {
    val inputList = File("input.txt").readLines()
    var sum = 0

    inputList.forEach { line ->
        val config = mutableMapOf<Int, List<String>>()
        val (digits, number) = line.split(" | ").map { it.split(" ").map { it.chunked(1) } }

        config[1] = findSegmentConfig(digits, 2)
        config[4] = findSegmentConfig(digits, 4)
        config[7] = findSegmentConfig(digits, 3)
        config[8] = findSegmentConfig(digits, 7)
        config[2] = findSegmentConfig(digits, 5) { it.count { it in config.getValue(4) } == 2 }
        config[3] = findSegmentConfig(digits, 5) { it.containsAll(config.getValue(1)) }
        config[5] = findSegmentConfig(digits, 5) { it != config.getValue(2) && it != config.getValue(3) }
        config[6] = findSegmentConfig(digits, 6) { !it.containsAll(config.getValue(1)) }
        config[9] = findSegmentConfig(digits, 6) { it.containsAll(config.getValue(4)) }
        config[0] = findSegmentConfig(digits, 6) { it != config.getValue(6) && it != config.getValue(9) }

        val segmentsToIntMap = config.entries.associate { (key, value) -> 
            value.sorted() to key 
        }

        sum += number.map { segmentsToIntMap.getValue(it.sorted()) }
            .joinToString("")
            .toInt()
    }

    println(sum)
}
