import java.io.File

fun main(args: Array<String>) {
    val measurementList = File("input.txt").readLines().map { it.toInt() }
    val numberOfIncrements = measurementList
        .zipWithNext()
        .count { (x, y) -> y > x }

    println(numberOfIncrements)
}