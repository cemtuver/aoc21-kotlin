import java.io.File
import kotlin.math.absoluteValue

fun main(args: Array<String>) {
    val inputList = File("input.txt").readLines()
    val positionList = inputList.first().split(",")
        .map { it.toInt() }
        .toIntArray()

    positionList.sort()

    val median = positionList[(positionList.size / 2)]
    val overallFuelCost = positionList.fold(0) { fuelCost, position ->
        fuelCost + (median - position).absoluteValue
    }

    println(overallFuelCost)
}