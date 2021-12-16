import java.io.File

data class Point(val x: Int, val y: Int, var cost: Int = 0) {

    val left: Point get() = Point(x, y - 1)
    val top: Point get() = Point(x - 1, y)
    val right: Point get() = Point(x, y + 1)
    val bottom: Point get() = Point(x + 1, y)
    val adjacents: List<Point> get() = listOf(left, top, right, bottom)

}

fun main(args: Array<String>) {
    val inputList = File("input.txt").readLines()
    val risks = expand(inputList.map { it.toCharArray().map { it.digitToInt()} })
    val costs = MutableList<MutableList<Int>>(risks.size) { MutableList<Int>(risks[0].size) { Int.MAX_VALUE } }    
    var path = mutableListOf<Point>()
    val visited = mutableListOf<Point>()
    var cost = 0

    path.add(Point(0, 0))
    costs[risks.size - 1][risks[0].size - 1] = risks.last().last()

    while (true) {
        var point = path.removeAt(0)

        if (point.x == risks.size - 1 && point.y == risks[0].size - 1) {
            cost = point.cost
            break
        } else {
            point.adjacents.forEach { adjacent ->
                if (adjacent.x in 0 until risks.size
                    && adjacent.y in 0 until risks[0].size
                    && visited.none { it.x == adjacent.x && it.y == adjacent.y }) {
                    adjacent.cost = point.cost + risks[adjacent.x][adjacent.y]

                    visited.add(adjacent)
                    path.add(adjacent)
                }
            }

            path = path.sortedBy { it.cost }.toMutableList()            
        }
    }

    println(cost)
}

fun expand(risks: List<List<Int>>): MutableList<MutableList<Int>> {
    var expandedRisks = MutableList(risks.size * 5) { MutableList(risks[0].size * 5) { 0 } }

    risks.forEachIndexed { x, row ->
        row.forEachIndexed { y, value ->
            expandedRisks[x][y] = value
        }
    }

    for (i in 0 until 5) {
        for (j in 0 until 5) {
            for (x in 0 until risks.size) {
                for (y in 0 until risks[0].size) {
                    if (i != 0 || j != 0) {
                        var value = 0
                        val left = ((i - 1) * risks.size) + x
                        val top = ((j - 1) * risks[0].size) + y
                        
                        if (left >= 0) {
                            value = expandedRisks[left][(j * risks[0].size) + y]
                        } else {
                            value = expandedRisks[(i * risks.size) + x][top]
                        }

                        value++
                        if (value == 10) value = 1

                        expandedRisks[(i * risks.size) + x][(j * risks[0].size) + y] = value
                    }
                }
            }
        }
    }
    
    return expandedRisks
}
