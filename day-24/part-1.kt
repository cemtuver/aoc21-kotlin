import java.io.File

abstract class Instruction() {

    abstract fun run(input: Input, registers: MutableMap<Char, Int>)

    fun getLiteral(registers: MutableMap<Char, Int>, operand: String): Int {
        return when (val literal = operand.toIntOrNull()) {
            null -> registers.getValue(operand[0])
            else -> literal
        }
    }

}

class InpInstruction(
    val operand1: Char
): Instruction() {

    override fun run(input: Input, registers: MutableMap<Char, Int>) {
        registers[operand1] = input.read()
    }

    override fun toString() = "inp $operand1"

}

class AddInstruction(
    val operand1: Char,
    val operand2: String
): Instruction() {

    override fun run(input: Input, registers: MutableMap<Char, Int>) {
        registers[operand1] = registers.getValue(operand1) + getLiteral(registers, operand2)
    }

    override fun toString() = "add $operand1 $operand2"

}

class MulInstruction(
    val operand1: Char,
    val operand2: String
): Instruction() {

    override fun run(input: Input, registers: MutableMap<Char, Int>) {
        registers[operand1] = registers.getValue(operand1) * getLiteral(registers, operand2)
    }

    override fun toString() = "mul $operand1 $operand2"

}

class DivInstruction(
    val operand1: Char,
    val operand2: String
): Instruction() {

    override fun run(input: Input, registers: MutableMap<Char, Int>) {
        registers[operand1] = (registers.getValue(operand1) / getLiteral(registers, operand2)).toInt()
    }

    override fun toString() = "div $operand1 $operand2"

}

class ModInstruction(
    val operand1: Char,
    val operand2: String
): Instruction() {

    override fun run(input: Input, registers: MutableMap<Char, Int>) {
        registers[operand1] = registers.getValue(operand1) % getLiteral(registers, operand2)
    }

    override fun toString() = "mod $operand1 $operand2"

}

class EqlInstruction(
    val operand1: Char,
    val operand2: String
): Instruction() {

    override fun run(input: Input, registers: MutableMap<Char, Int>) {
        registers[operand1] = when {
            registers.getValue(operand1) == getLiteral(registers, operand2) -> 1
            else -> 0
        }
    }

    override fun toString() = "eql $operand1 $operand2"

}

class Input(inputString: String) {

    private var index = 0
    private val input = inputString.map { it.toString().toInt() }

    fun read(): Int {
        return input[index++]
    }

}

class Alu() {

    private val registers: MutableMap<Char, Int> = mutableMapOf(
        'w' to 0,
        'x' to 0,
        'y' to 0,
        'z' to 0
    )

    fun run(instructions: List<Instruction>, inputString: String) {
        val input = Input(inputString)

        instructions.forEach {
            it.run(input, registers)
        }
    }

    fun load(aluState: AluState) {
        registers['w'] = aluState.w
        registers['x'] = aluState.x
        registers['y'] = aluState.y
        registers['z'] = aluState.z
    }

    fun save(): AluState {
        return AluState(
            registers.getValue('w'),
            registers.getValue('x'),
            registers.getValue('y'),
            registers.getValue('z')
        )
    }

    fun reset() {
        registers['w'] = 0
        registers['x'] = 0
        registers['y'] = 0
        registers['z'] = 0
    }

    override fun toString(): String {
        return "w: ${registers['w']}, x: ${registers['x']}, y: ${registers['y']}, z: ${registers['z']}"
    }

}

data class AluState(val w: Int, val x: Int, val y: Int, val z: Int)

fun main() {
    val instructions = File("input.txt").readLines().map { parseInstruction(it) }
    val partialInstructions = partialize(instructions)
    var stateMap = mutableMapOf<AluState, String>()
    val alu = Alu()

    (1..9).forEach { input ->
        alu.reset()
        alu.run(partialInstructions.getValue(14), input.toString())
        stateMap[alu.save()] = input.toString() 
    }

    (2..13).reversed().forEach { digit ->
        var newStateMap = mutableMapOf<AluState, String>()

        (1..9).forEach { input ->
            stateMap.forEach { state ->
                alu.load(state.key)
                try {
                    alu.run(partialInstructions.getValue(digit), input.toString())
                    newStateMap[alu.save()] = state.value + input.toString()
                } catch (exception: Exception) {
                    println(exception)
                }
            }
        }

        stateMap = newStateMap
    }

    var max = 0L

    (1..9).forEach { input ->
        stateMap.forEach { state ->
            alu.load(state.key)
            try {
                alu.run(partialInstructions.getValue(1), input.toString())
                
                if (alu.save().z == 0) {
                    val current = (state.value + input.toString()).toLong()
                    max = maxOf(max, current)
                }
            } catch (exception: Exception) {
                println(exception)
            }
        }
    }

    println(max)
}

fun partialize(instructions: List<Instruction>): Map<Int, MutableList<Instruction>> {
    var currentIndex = 15
    val partialInstructions = mutableMapOf<Int, MutableList<Instruction>>()

    instructions.forEach {
        if (it is InpInstruction) {
            currentIndex--
            partialInstructions[currentIndex] = mutableListOf<Instruction>()
        }

        partialInstructions.getValue(currentIndex).add(it)        
    }

    return partialInstructions
}

fun parseInstruction(string: String): Instruction {
    val input = string.split(" ")

    return when (input[0]) {
        "inp" -> InpInstruction(input[1][0])
        "add" -> AddInstruction(input[1][0], input[2])
        "mul" -> MulInstruction(input[1][0], input[2])
        "div" -> DivInstruction(input[1][0], input[2])
        "mod" -> ModInstruction(input[1][0], input[2])
        "eql" -> EqlInstruction(input[1][0], input[2])
        else -> throw Exception("Invalid instruction")
    }
}
