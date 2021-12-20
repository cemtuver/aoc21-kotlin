import java.io.File
import kotlin.math.absoluteValue

data class Target(val xMin: Int, val xMax: Int, val yMin: Int, val yMax: Int)

fun main() {
    val input = File("input.txt").readLines().first()
    val target = input.let { it.split("target area: x=", ", y=", "..") }
        .let { Target(it[1].toInt(), it[2].toInt(), it[3].toInt(), it[4].toInt()) }

    var y = target.yMin.absoluteValue - 1
    var height = y * (y + 1) / 2

    println(height)
}
