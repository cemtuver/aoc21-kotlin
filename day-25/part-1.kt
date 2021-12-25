import java.io.File

fun main() {
    val input = File("input.txt").readLines()
    var seafloor = input.map { it.toCharArray() }.toTypedArray()
    val maxRow = seafloor.size - 1
    val maxColumn = seafloor[0].size - 1
    var day = 1

    while (true) {
        var moved = false
        var newSeafloor = Array(maxRow + 1) { CharArray(maxColumn + 1) { '.' } }

        (0..maxRow).forEach { row ->
            (0..maxColumn).forEach { column ->
                if (seafloor[row][column] == '>') {
                    val nextColumn = when (column) {
                        maxColumn -> 0
                        else -> column + 1
                    }

                    if (seafloor[row][nextColumn] == '.') {
                        newSeafloor[row][nextColumn] = '>'
                        moved = true
                    } else {
                        newSeafloor[row][column] = '>'
                    }
                }
            }
        }

        (0..maxRow).forEach { row ->
            (0..maxColumn).forEach { column ->
                if (seafloor[row][column] == 'v') {
                    val nextRow = when (row) {
                        maxRow -> 0
                        else -> row + 1
                    }

                    if (newSeafloor[nextRow][column] == '.' && seafloor[nextRow][column] != 'v') {
                        newSeafloor[nextRow][column] = 'v'
                        moved = true
                    } else {
                        newSeafloor[row][column] = 'v'
                    }
                }
            }
        }

        if (!moved) break
        seafloor = newSeafloor
        day++
    }

    println(day)
}
