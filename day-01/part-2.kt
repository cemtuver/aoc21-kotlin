import java.io.File

const val WINDOW_SIZE = 3

fun main() {
    val measurementList = File("input.txt").readLines().map { it.toInt() }
    val numberOfIncrements = measurementList
        .windowed(size = WINDOW_SIZE)
        .map { it.sum() }
        .zipWithNext()
        .count { (x, y) -> y > x }

    println(numberOfIncrements)
}