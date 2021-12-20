import java.io.File

fun main() {
    val inputList = File("input.txt").readLines()
    val openChars = "([{<"
    val closeChars = ")]}>"
    val pointsMap = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137,
    )
    
    var sum = 0

    inputList.forEach { line ->
        val openCharList = mutableListOf<Char>()

        line.forEach charLoop@{ char -> 
            when (char) {
                in openChars -> openCharList.add(char)
                in closeChars -> {
                    val openChar = openCharList.removeAt(openCharList.size - 1)

                    if (openChars.indexOf(openChar) != closeChars.indexOf(char)) {
                        sum += pointsMap[char] ?: 0
                        return@charLoop
                    }
                }
            }
        }
    }

    println(sum)
}