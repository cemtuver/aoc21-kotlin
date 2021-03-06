import java.io.File

fun main() {
    val movementList = File("Input.txt").readLines()
    var horizontalPosition = 0
    var depth = 0
    var aim = 0

    movementList.forEach { movement ->
        var movementParts = movement.split(" ")
        var command = movementParts[0]
        var distance = movementParts[1].toInt()

        when (command) {
            "forward" -> {
                horizontalPosition += distance
                depth += distance * aim
            }
            "up" -> aim -= distance
            "down" -> aim += distance
        }
    }

    print(horizontalPosition * depth)
}