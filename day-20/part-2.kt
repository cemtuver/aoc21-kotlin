import java.io.File

fun main(args: Array<String>) {
    val input = File("input.txt").readLines()
    val algorithm = input.first().toCharArray()
    var inifitePixel = '.'
    var image = input.drop(2)
        .map { it.toCharArray() }
        .toTypedArray()

    repeat(50) {
        image = enhance(image, algorithm, inifitePixel)
        inifitePixel = when (inifitePixel) {
            '.' -> algorithm[0]
            else -> algorithm[511]
        }
    }

    println(image.sumOf { it.count { it == '#'} })
}

fun enhance(image: Array<CharArray>, algorithm: CharArray, default: Char): Array<CharArray> {
    val row = image.size + 2
    val column = image[0].size + 2
    val enhanced = Array(row) { CharArray(column) { default } }

    (0..row - 1).forEach { r ->
        (0..column - 1).forEach { c ->
            enhanced[r][c] = enhance(image, algorithm, r - 1, c - 1, default)
        }
    }

    return enhanced
}

fun enhance(image: Array<CharArray>, algorithm: CharArray, r: Int, c: Int, default: Char): Char {
    var index = 0

    if (get(image, r - 1, c - 1, default) == '#') index += 256
    if (get(image, r - 1, c, default) == '#') index += 128
    if (get(image, r - 1, c + 1, default) == '#') index += 64
    if (get(image, r, c - 1, default) == '#') index += 32
    if (get(image, r, c, default) == '#') index += 16
    if (get(image, r, c + 1, default) == '#') index += 8
    if (get(image, r + 1, c - 1, default) == '#') index += 4
    if (get(image, r + 1, c, default) == '#') index += 2
    if (get(image, r + 1, c + 1, default) == '#') index += 1

    return algorithm[index]
}

fun get(image: Array<CharArray>, r: Int, c: Int, default: Char): Char {
    return image.getOrNull(r)?.getOrNull(c) ?: default
}
