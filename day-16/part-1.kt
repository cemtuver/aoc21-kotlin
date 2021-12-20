import java.io.File

data class Packet(
    var length: Int,
    var version: Int, 
    var type: Int, 
    var literal: Long = 0,
    var children: List<Packet> = emptyList()
)

val hexToBinaryMap = mapOf(
        '0' to "0000",
        '1' to "0001",
        '2' to "0010",
        '3' to "0011",
        '4' to "0100",
        '5' to "0101",
        '6' to "0110",
        '7' to "0111",
        '8' to "1000",
        '9' to "1001",
        'A' to "1010",
        'B' to "1011",
        'C' to "1100",
        'D' to "1101",
        'E' to "1110",
        'F' to "1111"
    )

fun main() {
    val input = File("input.txt").readLines()
    val message = input.first()
        .map { hexToBinaryMap[it] }
        .joinToString("")

    val packet = parse(message)
    val sumOfVersions = sumVersions(packet)

    println(sumOfVersions)
}

fun parse(message: String): Packet {
    val version = message.take(3).toInt(2)
    val type = message.drop(3).take(3).toInt(2)
    var data = message.drop(6)

    if (type == 4) {
        var literal = ""
        var part = data.take(5)
        var length = 11

        literal += part.substring(1)        

        while (part.startsWith("1")) {
            data = data.drop(5)
            part = data.take(5)
            literal += part.substring(1)
            length += part.length
        }

        return Packet(length, version, type, literal.toLong(2))
    } else {
        val lengthType = data.take(1)

        if (lengthType == "1") {
            var length = 0
            val childCount = data.drop(1).take(11).toInt(2)
            val childList = mutableListOf<Packet>()

            for (i in 0 until childCount) {
                val child = parse(data.drop(12 + length))

                length += child.length
                childList.add(child)
            }

            return Packet(18 + length, version, type, -1, childList)
        } else {
            var length = 0
            val childLength = data.drop(1).take(15).toInt(2)
            val childList = mutableListOf<Packet>()

            while (length < childLength) {                
                val child = parse(data.drop(16 + length))

                length += child.length
                childList.add(child)
            }

            return Packet(22 + length, version, type, -1, childList)
        }
    }
}

fun sumVersions(packet: Packet): Int {
    return packet.version + packet.children.sumOf { sumVersions(it) }
}
