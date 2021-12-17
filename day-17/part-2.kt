import java.io.File
import kotlin.math.absoluteValue

data class Target(val xMin: Int, val xMax: Int, val yMin: Int, val yMax: Int)

fun main(args: Array<String>) {
    val input = File("input.txt").readLines().first()
    val target = input.let { it.split("target area: x=", ", y=", "..") }
        .let { Target(it[1].toInt(), it[2].toInt(), it[3].toInt(), it[4].toInt()) }

    var count = 0

    for (x in 1..target.xMax) {
        for (y in target.yMin..target.yMin.absoluteValue) {
            if (hits(x, y, target)) {
                count++
            }
        }
    }

    println(count)
}

fun hits(x: Int, y: Int, target: Target): Boolean {
    var xVelocity = x
    var yVelocity = y
    var xPosition = 0
    var yPosition = 0

    while (!exceeds(xPosition, yPosition, target)) {
        xPosition += xVelocity
        yPosition += yVelocity

        if (inside(xPosition, yPosition, target)) {
            return true
        }

        if (xVelocity > 0) xVelocity--
        yVelocity--
    }

    return false
}

fun exceeds(x: Int, y: Int, target: Target): Boolean {
    return x > target.xMax || y < target.yMin
}

fun inside(x: Int, y: Int, target: Target): Boolean {
    return x in target.xMin..target.xMax && y >= target.yMin && y <= target.yMax
}
