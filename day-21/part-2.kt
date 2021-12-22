import java.io.File

data class Player(val position: Int, val score: Int)
data class Game(val p1: Player, val p2: Player)

fun main() {
    val input = File("input.txt").readLines()
    val players = input.map { it.split(": ")[1] }
        .map { Player(it.toInt(), 0) }

    var p1Win = 0L
    var p2Win = 0L
    val games = mutableMapOf(Game(players[0], players[1]) to 1L)

    while (!games.isEmpty()) {
        val game = games.keys.first()
        val gameCount = games.getValue(game)
        games.remove(game)
        
        (1..3).forEach { p1d1 ->
            (1..3).forEach { p1d2 ->
                (1..3).forEach { p1d3 ->
                    val p1NewPosition = ((game.p1.position + p1d1 + p1d2 + p1d3 - 1) % 10) + 1
                    val p1NewScore = game.p1.score +  p1NewPosition
                    val p1New = Player(p1NewPosition, p1NewScore)

                    if(p1NewScore >= 21) {
                        p1Win += gameCount
                    } else {
                        (1..3).forEach { p2d1 ->
                            (1..3).forEach { p2d2 ->
                                (1..3).forEach { p2d3 ->
                                    val p2NewPosition = (game.p2.position + p2d1 + p2d2 + p2d3 - 1) % 10 + 1
                                    val p2NewScore = game.p2.score + p2NewPosition
                                    val p2New = Player(p2NewPosition, p2NewScore)
                                    
                                    if(p2NewScore >= 21) {
                                        p2Win+= gameCount
                                    } else {
                                        val gameNew = Game(p1New, p2New)
                                        games[gameNew] = games.getOrDefault(gameNew, 0L) + gameCount
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    println(maxOf(p1Win, p2Win))
}