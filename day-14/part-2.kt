import java.io.File

fun main(args: Array<String>) {
    val inputList = File("input.txt").readLines()
    val template = inputList.first()
    val rules = inputList.drop(2)
        .map { it.split(" -> ") }
        .associate { it[0] to it[1].first() }

    var pairCounts = template.windowed(2)
         .groupBy { it }
         .mapValues { it.value.size.toLong() }

    for (i in 0 until 40) {
        val newPairCounts: Map<String, Long> = buildMap {
            pairCounts.forEach {
                val insertion = rules[it.key]
                val left = "${it.key[0]}$insertion"
                val leftCount = getOrDefault(left, 0L) + it.value
                val right = "$insertion${it.key[1]}"
                val rightCount = getOrDefault(right, 0L) + it.value

                put(left, leftCount)
                put(right, rightCount)
            }
        }

        pairCounts = newPairCounts
    }

    val charCounts = mutableMapOf<Char, Long>()

    pairCounts.forEach {
        val right = it.key[1]
        val rightCount = charCounts.getOrDefault(right, 0L) + it.value

        charCounts.put(right, rightCount)
    }

    charCounts.put(template[0], charCounts[template[0]] ?: 1L)

    val max = charCounts.maxOf { it.value }
    val min = charCounts.minOf { it.value }

    println(max - min)
}