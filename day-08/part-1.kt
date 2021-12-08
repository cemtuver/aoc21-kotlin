import java.io.File

fun main(args: Array<String>) {
    val inputList = File("input.txt").readLines()
    val numberList = inputList.map { it.split(" | ")[1] }
    var sum = 0

    numberList.map { number ->
        number.split(" ").forEach { digit ->
            when (digit.length) {
                2, 4, 3, 7 -> sum++
            }
        }
    }
    
    println(sum)
}