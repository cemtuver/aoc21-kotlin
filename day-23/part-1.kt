import java.io.File
import kotlin.math.absoluteValue

val costMap = mapOf(
    'A' to 1,
    'B' to 10,
    'C' to 100,
    'D' to 1000,
)

data class Burrow(
    var hallway: String, 
    var aRoom: String,
    var bRoom: String,
    var cRoom: String,
    var dRoom: String
)

fun main() {
    val input = File("input.txt").readLines()
    val end = Burrow(
        "           ",
        "AA",
        "BB",
        "CC",
        "DD"
    )
    
    var current = parse(input)
    val doneMap = mutableMapOf<Burrow, Boolean>()
    val costMap = mutableMapOf(current to 0)

    while (current != end) {
        val currentCost = costMap.getValue(current)
        val adjacentCostMap = findAdjacentCosts(current)

        adjacentCostMap.forEach { 
            if (!doneMap.containsKey(it.key)) {
                val adjacent = it.key
                val adjacentCost = it.value + currentCost

                costMap[adjacent] = minOf(costMap.getOrDefault(adjacent, Int.MAX_VALUE), adjacentCost)
            }
        }

        doneMap[current] = true
        costMap.remove(current)
        
        current = findBurrowWithMinimumCost(costMap)
    }

    println(costMap[end])
}

fun findAdjacentCosts(burrow: Burrow): MutableMap<Burrow, Int> {
    val adjacentCostMap = mutableMapOf<Burrow, Int>()

    adjacentCostMap.putAll(findExitRoomAdjacentCosts(burrow))
    adjacentCostMap.putAll(findEnterRoomAdjacentCosts(burrow))

    return adjacentCostMap
}

fun findEnterRoomAdjacentCosts(burrow: Burrow): MutableMap<Burrow, Int> {
    val enterRoomAdjacentCosts = mutableMapOf<Burrow, Int>()

    enterRoomAdjacentCosts.putAll(findEnterRoomAdjacentCosts(burrow.aRoom, burrow, 'A', 2))
    enterRoomAdjacentCosts.putAll(findEnterRoomAdjacentCosts(burrow.bRoom, burrow, 'B', 4))
    enterRoomAdjacentCosts.putAll(findEnterRoomAdjacentCosts(burrow.cRoom, burrow, 'C', 6))
    enterRoomAdjacentCosts.putAll(findEnterRoomAdjacentCosts(burrow.dRoom, burrow, 'D', 8))

    return enterRoomAdjacentCosts
}

fun findEnterRoomAdjacentCosts(room: String, burrow: Burrow, enterChar: Char, enterIndex: Int): MutableMap<Burrow, Int> {
    if (!room.replace(enterChar, ' ').isBlank()) return mutableMapOf()

    val enterRoomAdjacentCosts = mutableMapOf<Burrow, Int>()

    val hallway = burrow.hallway
    var roomIndex = 0
    val movingCharCost = costMap.getValue(enterChar)

    while (roomIndex <= room.length - 1 && room[roomIndex] == ' ') roomIndex++
    roomIndex--

    hallway.forEachIndexed { i, c ->
        if (c == enterChar) {
            val startIndex = minOf(i, enterIndex)
            val endIndex = maxOf(i, enterIndex)
            val path = hallway.substring(startIndex + 1, endIndex)

            if (path.isBlank()) {
                val newBurrow = burrow.copy()
                val newHallway = hallway.toCharArray()
                val newRoom = room.toCharArray()

                newHallway[i] = ' '
                newRoom[roomIndex] = enterChar

                newBurrow.hallway = newHallway.joinToString("")
                when (enterIndex) {
                    2 -> newBurrow.aRoom = newRoom.joinToString("")
                    4 -> newBurrow.bRoom = newRoom.joinToString("")
                    6 -> newBurrow.cRoom = newRoom.joinToString("")
                    8 -> newBurrow.dRoom = newRoom.joinToString("")
                }

                enterRoomAdjacentCosts[newBurrow] = (endIndex - startIndex + roomIndex + 1) * movingCharCost
            }
        }
    }

    return enterRoomAdjacentCosts
}

fun findExitRoomAdjacentCosts(burrow: Burrow): MutableMap<Burrow, Int> {
    val exitRoomAdjacentCosts = mutableMapOf<Burrow, Int>()

    exitRoomAdjacentCosts.putAll(findExitRoomAdjacentCosts(burrow.aRoom, burrow, 2))
    exitRoomAdjacentCosts.putAll(findExitRoomAdjacentCosts(burrow.bRoom, burrow, 4))
    exitRoomAdjacentCosts.putAll(findExitRoomAdjacentCosts(burrow.cRoom, burrow, 6))
    exitRoomAdjacentCosts.putAll(findExitRoomAdjacentCosts(burrow.dRoom, burrow, 8))

    return exitRoomAdjacentCosts
}

fun findExitRoomAdjacentCosts(room: String, burrow: Burrow, enterIndex: Int): MutableMap<Burrow, Int> {
    if (room.isBlank()) return mutableMapOf()

    val exitRoomAdjacentCosts = mutableMapOf<Burrow, Int>()

    val hallway = burrow.hallway
    var movingIndex = 0
    var leftMostIndex = enterIndex - 1
    var rightMostIndex = enterIndex + 1

    while (room[movingIndex] == ' ') movingIndex++
    while (leftMostIndex >= 0 && hallway[leftMostIndex] == ' ') leftMostIndex--
    while (rightMostIndex <= 10 && hallway[rightMostIndex] == ' ') rightMostIndex++

    leftMostIndex++
    rightMostIndex--
    val movingChar = room[movingIndex]
    val movingCharCost = costMap.getValue(movingChar)

    (leftMostIndex..rightMostIndex).forEach { p ->
        if (p != 2 && p != 4 && p != 6 && p != 8) {
            val newBurrow = burrow.copy()
            val newHallway = hallway.toCharArray()
            val newRoom = room.toCharArray()

            newHallway[p] = room[movingIndex]
            newRoom[movingIndex] = ' '

            newBurrow.hallway = newHallway.joinToString("")
            when (enterIndex) {
                2 -> newBurrow.aRoom = newRoom.joinToString("")
                4 -> newBurrow.bRoom = newRoom.joinToString("")
                6 -> newBurrow.cRoom = newRoom.joinToString("")
                8 -> newBurrow.dRoom = newRoom.joinToString("")
            }

            exitRoomAdjacentCosts[newBurrow] = ((movingIndex + 1) + (p - enterIndex).absoluteValue) * movingCharCost
        }
    }

    return exitRoomAdjacentCosts
}

fun findBurrowWithMinimumCost(map: MutableMap<Burrow, Int>): Burrow {
    var minimumCost = map.values.first()
    var burrowWithMinimumCost = map.keys.first()

    map.forEach {
        if (it.value < minimumCost) {
            burrowWithMinimumCost = it.key
            minimumCost = it.value
        }
    }

    return burrowWithMinimumCost
}

fun print(burrow: Burrow, cost: Int) {
    println("Cost: $cost")
    println("#############")
    println("#${burrow.hallway}#")
    println("###${burrow.aRoom[0]}#${burrow.bRoom[0]}#${burrow.cRoom[0]}#${burrow.dRoom[0]}###")
    println("  #${burrow.aRoom[1]}#${burrow.bRoom[1]}#${burrow.cRoom[1]}#${burrow.dRoom[1]}#  ")
    println("  #########  ")
    println()
}

fun parse(input: List<String>): Burrow {
    val burrow = Burrow(
        "           ",
        "${input[2][3]}${input[3][3]}",
        "${input[2][5]}${input[3][5]}",
        "${input[2][7]}${input[3][7]}",
        "${input[2][9]}${input[3][9]}",
    )

    return burrow
}
