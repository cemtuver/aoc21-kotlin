import java.io.File

const val ROW = 5
const val COLUMN = 5
val SPACE_REPLACE_REGEX = "\\s+".toRegex()

data class Board(
    val numbers: Array<IntArray> = Array(ROW) { IntArray(COLUMN) },
    val marked: Array<BooleanArray> = Array(ROW) { BooleanArray(COLUMN) { false } },
    var pickedWinnerNumber: Int = 0
) {
    private fun winRow(row: Int): Boolean {
        for (i in 0 until COLUMN) {
            if (!marked[row][i]) {
                return false
            }
        }

        return true
    }

    private fun winColumn(column: Int): Boolean {
        for (i in 0 until ROW) {
            if (!marked[i][column]) {
                return false
            }
        }

        return true
    }

    fun score(): Int {
        var sum = 0

        for (i in 0 until ROW) {
            for (j in 0 until COLUMN) {
                if (!marked[i][j]) {
                    sum = sum + numbers[i][j]
                }
            }
        }

        return sum * pickedWinnerNumber
    }

    fun mark(number: Int): Boolean {
        for (i in 0 until ROW) {
            for (j in 0 until COLUMN) {
                if (numbers[i][j] == number) {
                    marked[i][j] = true

                    if (winRow(i) || winColumn(j)) {
                        pickedWinnerNumber = number
                        return true
                    }
                }
            }
        }

        return false
    }

    fun print() {
        for (i in 0 until ROW) {
            for (j in 0 until COLUMN) {
                print(" ${numbers[i][j]}")

                if (marked[i][j]) {
                    print("*")
                }
            }
            
            println()
        }

        println("---")
    }
}  

fun play(drawnList: List<Int>, boardList: List<Board>): Board? {
    drawnList.forEach { drawn ->
        boardList.forEach { board ->
            if (board.mark(drawn)) {
                return board
            }
        }
    }

    return null
}

fun main() {
    val inputList = File("input.txt").readLines()
    val drawnList = inputList.first().split(",").map { it.toInt() }
    val boardList = mutableListOf<Board>()

    for (i in 2 until inputList.size step 6) {
        val board = Board()

        for (j in 0 until ROW) {
            val line = inputList[i + j].trim()
            val numbers = line.split(SPACE_REPLACE_REGEX)

            for (k in 0 until COLUMN) {
                board.numbers[j][k] = numbers[k].trim().toInt()
            }
        }

        boardList.add(board)
    }

    val winnerBoard = play(drawnList, boardList)

    if (winnerBoard != null) {
        println(winnerBoard.score())
    }
}