import java.io.File

data class Point(val x: Int, val y: Int) {

    val left: Point get() = Point(x, y - 1)
    val top: Point get() = Point(x - 1, y)
    val right: Point get() = Point(x, y + 1)
    val bottom: Point get() = Point(x + 1, y)
    val adjacents: List<Point> get() = listOf(left, top, right, bottom)

    fun value(array: List<IntArray>, default: Int = Int.MAX_VALUE): Int {
        return when {
            x in 0 until array.size && y in 0 until array[0].size -> array[x][y]
            else -> default
        }
    }

}

fun main() {
    val inputList = File("input.txt").readLines()
    val array = inputList.map { line -> line.toList().map { it.digitToInt() }.toIntArray() }
    val basinList = mutableListOf<Int>()

    array.forEachIndexed { x, columnArray ->
        columnArray.forEachIndexed { y, height ->
            if (Point(x, y).adjacents.all { it.value(array) > height }) {
                val basin = countBasin(
                    array,
                    Array<BooleanArray>(array.size) { BooleanArray(columnArray.size) { false } },
                    x, 
                    y
                )

                basinList.add(basin)
            }
        }
    }

    println(basinList.sortedDescending().take(3).fold(1) { result, basin -> result * basin })
}

fun countBasin(array: List<IntArray>, visited: Array<BooleanArray>, x: Int, y: Int): Int {
    if (array[x][y] == 9) return 0
    visited[x][y] = true

    var count = 1
    var height = array[x][y]

    Point(x, y).adjacents.forEach { adjacent ->
        if (adjacent.value(array, Int.MIN_VALUE) > height && !visited[adjacent.x][adjacent.y]) {
            count += countBasin(array, visited, adjacent.x, adjacent.y)
        }
    }

    return count
}