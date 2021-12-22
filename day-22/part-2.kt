import java.io.File
import kotlin.math.absoluteValue

data class Cuboid(
    val xMin: Int, 
    val xMax: Int, 
    val yMin: Int, 
    val yMax: Int,
    val zMin: Int,
    val zMax: Int
) {

    fun intersect(cuboid: Cuboid): Cuboid? {
        val xMin = maxOf(xMin, cuboid.xMin)
        val xMax = minOf(xMax, cuboid.xMax)
        val yMin = maxOf(yMin, cuboid.yMin)
        val yMax = minOf(yMax, cuboid.yMax)
        val zMin = maxOf(zMin, cuboid.zMin)
        val zMax = minOf(zMax, cuboid.zMax)
        val intersect = xMin <= xMax && yMin <= yMax && zMin <= zMax

        return when {
            intersect -> Cuboid(xMin, xMax, yMin, yMax, zMin, zMax)
            else -> null
        }
    }

}

fun main() {
    val input = File("input.txt").readLines()
    val steps = input.map { it.split(" x=", ",y=", ",z=", "..") }
        .map { Cuboid(it[1].toInt(), it[2].toInt(), it[3].toInt(), it[4].toInt(), it[5].toInt(), it[6].toInt()) to (it[0] == "on") }
        .toMap()

    var shape = mutableMapOf<Cuboid, Long>()

    steps.forEach { s ->
        var newShape = mutableMapOf<Cuboid, Long>()

        shape.forEach { c -> 
            s.key.intersect(c.key)?.let { i -> 
                newShape[i] = newShape.getOrDefault(i, 0L) - c.value
            }
        }

        if (s.value) {
            newShape[s.key] = newShape.getOrDefault(s.key, 0L) + 1
        }

        newShape.forEach { ns ->
            shape[ns.key] = shape.getOrDefault(ns.key, 0L) + ns.value
        }
    }

    var onCount = 0L

    shape.forEach {
        val cuboid = it.key
        val numberOfCubes = (cuboid.xMax - cuboid.xMin + 1L) * (cuboid.yMax - cuboid.yMin + 1L) * (cuboid.zMax - cuboid.zMin + 1L)

        onCount += numberOfCubes * it.value
    }

    println(onCount)
}
