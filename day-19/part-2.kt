import java.io.File
import kotlin.math.absoluteValue

data class Position(val x: Int, val y: Int, val z: Int) {

    fun variant(r: Int, u: Int): Position {
        val uv = up(u)
        val ruv = uv.rotate(r)

        return ruv
    }

    fun rotate(r: Int): Position {
        return when (r) {
            1 -> Position(-y, x, z)
            2 -> Position(-x, -y, z)
            3 -> Position(y, -x, z)
            else -> this
        }
    }

    fun up(u: Int): Position {
        return when (u) {
            1 -> Position(x, -y, -z)
            2 -> Position(x, -z, y)
            3 -> Position(-y, -z, x)
            4 -> Position(-x, -z, -y)
            5 -> Position(y, -z, -x)
            else -> this;
        }
    }

}

data class Scanner(val readings: MutableList<Position> = mutableListOf()) {

    fun variants(): List<Scanner> {
        val variants = MutableList<Scanner>(24) { Scanner() }

        (0..5).forEach { u ->
            (0..3).forEach { r ->
                val variant = variants[u * 4 + r]
                readings.forEach { variant.readings.add(it.variant(r, u))}
            }
        }

        return variants
    }

}

fun main() {
    val input = File("input.txt").readLines()
    val scannerList = mutableListOf<Scanner>()

    input.forEach {
        if (it == "") return@forEach
        else if (it.startsWith("---")) scannerList.add(Scanner())
        else {
            val reading = it.split(",").let { 
                Position(it[0].toInt(), it[1].toInt(), it[2].toInt()) 
            }

            scannerList.last().readings.add(reading)
        }
    }

    var maxDistance = 0
    val scanners = findScanners(scannerList)

    scanners.forEach { p1 ->
        scanners.forEach { p2 ->
            val d = (p1.x - p2.x).absoluteValue + (p1.y - p2.y).absoluteValue + (p1.z - p2.z).absoluteValue 
            maxDistance = maxOf(maxDistance, d)
        }
    }

    println(maxDistance)
}

fun findScanners(scannerList: List<Scanner>): List<Position> {
    val s = scannerList.first()
    val dList = scannerList.drop(1).toMutableList()
    val spList = mutableListOf<Position>()
    
    while (!dList.isEmpty()) {
        val d = dList.removeAt(0)
        var newReadings = mutableListOf<Position>()
        var matched = false

        run loop@ {
            s.variants().forEach { sv ->
                d.variants().forEach { dv ->
                    sv.readings.forEach { svr ->
                        dv.readings.forEach { dvr ->
                            var matchCount = 0
                            val xdiff = dvr.x - svr.x
                            val ydiff = dvr.y - svr.y
                            val zdiff = dvr.z - svr.z

                            sv.readings.forEach { r1 ->
                                dv.readings.forEach { r2 ->
                                    val x = r1.x + xdiff
                                    val y = r1.y + ydiff
                                    val z = r1.z + zdiff

                                    if (x == r2.x && y == r2.y && z == r2.z) {
                                        matchCount++  
                                    }
                                }
                            }

                            if (matchCount >= 12) {
                                var list = mutableSetOf<Position>()                        

                                sv.readings.forEach { list.add(it) }
                                dv.readings.forEach {
                                    list.add(
                                        Position(
                                            it.x - xdiff,
                                            it.y - ydiff,
                                            it.z - zdiff
                                        )
                                    )
                                }                                

                                matched = true
                                newReadings = list.toMutableList()
                                spList.add(Position(-xdiff, -ydiff, -zdiff))
                                return@loop
                            }
                        }
                    }
                }
            }
        }

        if (matched) {
            s.readings.clear()
            s.readings.addAll(newReadings)
        } else {
            dList.add(d)
        }
    }

    return spList
}
