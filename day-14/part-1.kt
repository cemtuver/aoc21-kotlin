import java.io.File

fun main() {
    val inputList = File("input.txt").readLines()
    var template = inputList.first()
    val rules = inputList.drop(2)
        .map { it.split(" -> ") }
        .associate { it[0] to it[1] }
        .toMap()

    for (i in 0 until 10) {
        var polymer = ""

        template.windowed(2)
            .map { it.toCharArray() }
            .forEach {
                polymer += it[0].toString() + rules[it[0].toString() + it[1].toString()]
            }

        polymer += template.last().toString()
        template = polymer
    }

    val counts = template.groupBy { it }
    val max = counts.maxOf { it.value.size }
    val min = counts.minOf { it.value.size }

    println(max - min)
}