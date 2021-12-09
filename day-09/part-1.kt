import java.io.File

data class Point(val x: Int, val y: Int) {

    val left: Point get() = Point(x, y - 1)
    val top: Point get() = Point(x - 1, y)
    val right: Point get() = Point(x, y + 1)
    val bottom: Point get() = Point(x + 1, y)
    val adjacents: List<Point> get() = listOf(left, top, right, bottom)

    fun value(array: List<IntArray>): Int {
        return when {
            x in 0 until array.size && y in 0 until array[0].size -> array[x][y]
            else -> Int.MAX_VALUE
        }
    }

}

fun main(args: Array<String>) {
    val inputList = File("input.txt").readLines()
    val array = inputList.map { line -> line.toList().map { it.digitToInt() }.toIntArray() }
    var sum = 0

    array.forEachIndexed { x, columnArray ->
        columnArray.forEachIndexed { y, height ->
            if (Point(x, y).adjacents.all { it.value(array) > height }) {
                sum += height + 1
            }
        }
    }

    println(sum)
}