import java.io.File

data class Player(var position: Int, var score: Int)

fun main() {
    val input = File("input.txt").readLines()
    val players = input.map { it.split(": ")[1] }
        .map { Player(it.toInt(), 0) }

    var dice = 1
    var turn = 0
    var rolls = 0

    while (true) {
        val player = players[turn % 2]

        repeat(3) {
            player.position += dice - 1
            player.position %= 10
            player.position++
            if (++dice > 100) { dice = 1 }
        }

        rolls += 3
        turn++

        player.score += player.position
        if (player.score >= 1000) break
    }

    println(players.minOf { it.score } * rolls)
}
