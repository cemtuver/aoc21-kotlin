import java.io.File

data class Point(val x: Int, val y: Int)
data class Fold(val axis: String, val index: Int)

fun main(args: Array<String>) {
    val inputList = File("input.txt").readLines()
    val dividerIndex = inputList.indexOf("")
    var pointList = inputList.take(dividerIndex)
        .map{ it.split(",") }
        .map { Point(it[0].toInt(), it[1].toInt()) }
        .toSet()

    val foldList = inputList.takeLast(inputList.size - dividerIndex - 1)
        .map{ it.replace("fold along ", "").split("=") }
        .map { Fold(it[0], it[1].toInt()) }
    
    val fold = foldList.first()

    pointList = pointList.map { point ->
        when {
            fold.axis == "y" && point.y > fold.index -> point.copy(y = 2 * fold.index - point.y) 
            fold.axis == "x" && point.x > fold.index -> point.copy(x = 2 * fold.index - point.x)
            else -> point
        }
    }.toSet()

    println(pointList.size)
}