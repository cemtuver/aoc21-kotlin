import java.io.File

fun main(args: Array<String>) {
    val inputList = File("input.txt").readLines()
    val spawnDayCountMap = mutableMapOf<Int, Long>()
    val initialFishList = inputList.first().split(",")
        .map { it.toInt() }

    initialFishList.forEach { fish ->
        spawnDayCountMap[fish] = (spawnDayCountMap[fish] ?: 0) + 1
    }

    for (day in 1..256) {
        val newFishCount = spawnDayCountMap[0] ?: 0

        for (i in 1..8) {
            spawnDayCountMap[i - 1] = spawnDayCountMap[i] ?: 0
        }

        spawnDayCountMap[6] = (spawnDayCountMap[6] ?: 0) + newFishCount
        spawnDayCountMap[8] = newFishCount
    }

    val totalFishCount = spawnDayCountMap.map { it.value }
        .sum()
    
    println(totalFishCount)
}