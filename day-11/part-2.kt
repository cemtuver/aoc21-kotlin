import java.io.File

data class Point(val x: Int, val y: Int) {

    val left: Point get() = Point(x, y - 1)
    val topLeft: Point get() = Point(x - 1, y - 1)
    val top: Point get() = Point(x - 1, y)
    val topRight: Point get() = Point (x - 1, y + 1)
    val right: Point get() = Point(x, y + 1)
    val bottomRight: Point get() = Point(x + 1, y + 1)
    val bottom: Point get() = Point(x + 1, y)
    val bottomLeft: Point get() = Point(x + 1 , y - 1)
    val adjacents: List<Point> get() = listOf(left, topLeft, top, topRight, right, bottomLeft, bottom, bottomRight)

    fun value(array: List<IntArray>): Int? {
        return when {
            x in 0 until array.size && y in 0 until array[0].size -> array[x][y]
            else -> null
        }
    }

}

fun main(args: Array<String>) {
    val inputList = File("input.txt").readLines()
    val array = inputList.map { line -> line.toList().map { it.digitToInt() }.toIntArray() }
    var step = 0

    while (true) {
        var flashedCount = 0
        val visited = Array<BooleanArray>(array.size) { BooleanArray(array[0].size) { false } } 

        step++

        array.forEachIndexed { x, columnArray ->
            columnArray.forEachIndexed { y, energy ->
                array[x][y] = energy + 1
            }
        }

        array.forEachIndexed { x, columnArray ->
            columnArray.forEachIndexed { y, energy ->
                if (energy > 9) {
                    flash(array, visited, Point(x, y))
                }
            }
        }

        array.forEachIndexed { x, columnArray ->
            columnArray.forEachIndexed { y, energy ->
                if (energy > 9) {
                    array[x][y] = 0
                    flashedCount++
                }
            }
        }

        if (flashedCount == array.size * array[0].size) {
            break
        }
    }

    println(step)
}

fun flash(array: List<IntArray>, visited: Array<BooleanArray>, point: Point) {
    if (visited[point.x][point.y]) return
    
    visited[point.x][point.y] = true
    point.adjacents.forEach { adjacent ->
        adjacent.value(array)?.let { adjacentValue ->
            if (adjacentValue != 0) {
                array[adjacent.x][adjacent.y] = adjacentValue + 1

                if (adjacentValue == 9) {
                    flash(array, visited, adjacent)
                }
            }
        }
    }
}