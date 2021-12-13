import java.io.File

fun main(args: Array<String>) {
    val inputList = File("input.txt").readLines()
    val edges = inputList.map { input -> input.split("-") }
        .flatMap { listOf(it.first() to it.last(), it.last() to it.first() ) }
        .groupBy({ it.first }, { it.second })

    println(findPaths(edges, listOf("start")))
}

fun findPaths(edges: Map<String, List<String>>, path: List<String>): Int {
    var count = 0
    val currentNode = path.last()

    if (currentNode == "end") {
        count = 1
    } else {
        edges.getValue(currentNode)
            .filter { 
                it != "start" 
                && 
                (
                    it[0].isUpperCase()
                    || it !in path
                    || path.filterNot { it[0].isUpperCase() }
                        .groupBy { it }
                        .all { it.value.size < 2 }        
                )              
             }
            .forEach { count += findPaths(edges, path + it) }
    }

    return count
}