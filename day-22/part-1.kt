import java.io.File

data class Cuboid(
    val on: Boolean,
    val xMin: Int, 
    val xMax: Int, 
    val yMin: Int, 
    val yMax: Int,
    val zMin: Int,
    val zMax: Int
)

fun main() {
    val input = File("input.txt").readLines()
    val cube = Array(101) { Array(101) { BooleanArray(101) { false } } }
    val cuboids = input.map { it.split(" x=", ",y=", ",z=", "..") }
        .map { 
            Cuboid(
                it[0] == "on",
                it[1].toInt() + 50, 
                it[2].toInt() + 50, 
                it[3].toInt() + 50, 
                it[4].toInt() + 50, 
                it[5].toInt() + 50, 
                it[6].toInt() + 50, 
            ) 
        }
        .filter { 
            it.xMin > 0 && it.xMax < 100 
                && it.yMin > 0 && it.yMax < 100 
                && it.zMin > 0 && it.zMax < 100 
        }
    
    var onCount = 0

    cuboids.forEach { cuboid ->
        (cuboid.xMin..cuboid.xMax).forEach { x ->
            (cuboid.yMin..cuboid.yMax).forEach { y ->
                (cuboid.zMin..cuboid.zMax).forEach { z ->
                    if (cube[x][y][z] && !cuboid.on) {
                        onCount--
                    } else if (!cube[x][y][z] && cuboid.on) {
                        onCount++
                    }

                    cube[x][y][z] = cuboid.on
                }
            }
        }
    }

    println(onCount)
}