import java.io.File

fun main(args: Array<String>) {
    val movementList = File("Input.txt").readLines()
    var horizontalPosition = 0
    var depth = 0

    movementList.forEach { movement ->
        var movementParts = movement.split(" ")
        var command = movementParts[0]
        var distance = movementParts[1].toInt()

        when (command) {
            "forward" -> horizontalPosition += distance
            "up" -> depth -= distance
            "down" -> depth += distance
        }
    }

    print(horizontalPosition * depth)
}