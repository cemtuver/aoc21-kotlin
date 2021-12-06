import java.io.File

const val ROW = 1000
const val COLUMN = 1000

data class Point(
    var x: Int = 0,
    var y: Int = 0
) {
    constructor(pointString: String) : this() {
        pointString.split(",").also {
            x = it[0].toInt()
            y = it[1].toInt()
        }
    }
}

data class Vent(
    val startPoint: Point,
    val endPoint: Point
) {
    val isHorizontalOrVertical: Boolean 
        get() = startPoint.x == endPoint.x || startPoint.y == endPoint.y

    val line: List<Point>
        get() {
            val pointList = mutableListOf<Point>()

            if (isHorizontalOrVertical) {
                createHorizontalOrVerticalLine(pointList)
            } else {
                createDiagonalLine(pointList)
            }

            return pointList
        }

    private fun createHorizontalOrVerticalLine(pointList: MutableList<Point>) {
        pointList.add(startPoint)

        if (startPoint.x == endPoint.x) {
            var startY = minOf(startPoint.y, endPoint.y)
            var endY = maxOf(startPoint.y, endPoint.y)

            for (i in startY + 1 until endY) {
                pointList.add(Point(startPoint.x, i))
            }
        } else if (startPoint.y == endPoint.y) {
            var startX = minOf(startPoint.x, endPoint.x)
            var endX = maxOf(startPoint.x, endPoint.x)

            for (i in startX + 1 until endX) {
                pointList.add(Point(i, startPoint.y))
            }
        }

        pointList.add(endPoint)
    }

    private fun createDiagonalLine(pointList: MutableList<Point>) {
        val diffX = maxOf(minOf(endPoint.x - startPoint.x, 1), -1)
        val diffY = maxOf(minOf(endPoint.y - startPoint.y, 1), -1)
        var step = endPoint.x - startPoint.x
        
        step = when {
            step > 0 -> step
            else -> step * -1
        }

        for (i in 0..step) {
            val x = startPoint.x + diffX * i
            val y = startPoint.y + diffY * i

            pointList.add(Point(x, y))
        }
    }

}

fun main(args: Array<String>) {
    val lines = File("input.txt").readLines()
    val overlap = Array(row) { IntArray(column) { 0 } }
    var numberOfOverlaps = 0

    lines.forEach { line ->
        var points = line.split(" -> ")
        val vent = Vent(
            Point(points[0]),
            Point(points[1])
        )

        vent.line.forEach { linePoint ->
            var currentOverlap = overlap[linePoint.x][linePoint.y]

            if (currentOverlap == 1) {
                numberOfOverlaps++
            }

            overlap[linePoint.x][linePoint.y] = currentOverlap + 1
        }
    }

    println(numberOfOverlaps)
}