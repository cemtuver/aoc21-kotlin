import java.io.File

fun main() {
    val inputList = File("input.txt").readLines()
    val openChars = "([{<"
    val closeChars = ")]}>"
    val pointsMap = mapOf(
        ')' to 1L,
        ']' to 2L,
        '}' to 3L,
        '>' to 4L,
    )

    val scoreList = mutableListOf<Long>()

    inputList.forEach lineLoop@{ line ->
        var sum = 0L
        val openCharList = mutableListOf<Char>()
        val closeCharList = mutableListOf<Char>()

        line.forEach charLoop@{ char -> 
            when (char) {
                in openChars -> openCharList.add(char)
                in closeChars -> {
                    val openChar = openCharList.removeAt(openCharList.count() - 1)

                    if (openChars.indexOf(openChar) != closeChars.indexOf(char)) {
                        return@lineLoop                    
                    }
                }
            }
        }

        openCharList.asReversed().forEach { openChar ->
            sum *= 5L
            sum += pointsMap[closeChars[openChars.indexOf(openChar)]] ?: 0L
        }

        scoreList.add(sum)
    }

    println(scoreList.sorted().run { get(size / 2) })
}