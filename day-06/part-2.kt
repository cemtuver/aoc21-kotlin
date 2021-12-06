import java.io.File

const val SIMULATION_DAY = 256
const val FISH_SPAWN_INTERVAL = 6
const val FISH_SPAWN_INTERVAL_DELAY = 2

fun setFishSpawnDays(startDay: Int, spawnFishCount: Long, spawnDayFisCountMap: MutableMap<Int, Long>) {
    var spawnDay = startDay

    while (spawnDay <= SIMULATION_DAY) {
        spawnDayFisCountMap[spawnDay] = (spawnDayFisCountMap[spawnDay] ?: 0) + spawnFishCount
        spawnDay += FISH_SPAWN_INTERVAL + 1
    }
}

fun main(args: Array<String>) {
    val inputList = File("input.txt").readLines()
    val initialSpawnDayList = inputList.first().split(",").map { it.toInt() }
    val spawnDayFisCountMap = mutableMapOf<Int, Long>()
    var numberOfFish = initialSpawnDayList.count().toLong()

    initialSpawnDayList.forEach {
        setFishSpawnDays(it + 1, 1, spawnDayFisCountMap)
    }

    for (i in 1 until SIMULATION_DAY + 1) {
        val newFishCount = spawnDayFisCountMap[i] ?: continue

        setFishSpawnDays(
            i + FISH_SPAWN_INTERVAL + FISH_SPAWN_INTERVAL_DELAY + 1, 
            newFishCount, 
            spawnDayFisCountMap
        )

        numberOfFish += newFishCount
    }
    
    println(numberOfFish)
}