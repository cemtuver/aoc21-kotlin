import java.io.File
import kotlin.math.absoluteValue

fun main(args: Array<String>) {
    val inputList = File("input.txt").readLines()
    val positionList = inputList.first().split(",")
        .map { it.toInt() }
        .toIntArray()

    val min = positionList.minOrNull() ?: 0
    val max = positionList.maxOrNull() ?: 0
    var overallFuelCost = Int.MAX_VALUE

    for (medianPoint in min..max) {
        var fuelCostWithCurrentMedianPoint = positionList.fold(0) { fuelCost, element -> 
            val fuelCostForCurrentElement = (element - medianPoint).absoluteValue
            fuelCost + (fuelCostForCurrentElement * (fuelCostForCurrentElement + 1) / 2)
        }

        overallFuelCost = minOf(fuelCostWithCurrentMedianPoint, overallFuelCost)
    }

    println(overallFuelCost)
}