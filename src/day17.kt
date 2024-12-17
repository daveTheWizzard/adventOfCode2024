import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.*
import kotlin.time.times

var A: Long = 0
var B: Long = 0
var C: Long = 0
var program: List<Int> = listOf()
var instructionPointer = 0

fun main(){
    val input = readInput("input_day17")
    parseInput(input)
    val A_backup = A
    val B_backup = B
    val C_backup = C
    println("Register A: $A\nRegister B: $B\nRegister C: $C\nProgram: $program")
    var output: List<Int> = listOf()
    var i: Long = 0
    while(output != program){
        A = A_backup
        B = B_backup
        C = C_backup
        instructionPointer = 0
        i += 1
        A = i
        output = executeProgram()
    }
    println("Found copy after $i many steps")
}

private fun readInput(name: String)= Path("input/$name.txt").readLines()

private fun parseInput(input: List<String>) {
    input.forEach {
        val getRegister = "Register (?<register>[A-C]): (?<value>[0-9]+)".toRegex()
        val matchReg = getRegister.matchEntire(it)
        if (matchReg != null) {
            val value = matchReg.groups["value"]!!.value.toLong()
            when (matchReg.groups["register"]!!.value.first().toChar()) {
                'A' -> A = value.toLong()
                'B' -> B = value
                'C' -> C = value
            }
        }
        val getProgram = "Program: (?<numbers>[0-9+(,|?)]+)".toRegex()
        val matchProgram = getProgram.matchEntire(it)
        if (matchProgram != null) {
            val numbers = matchProgram.groups["numbers"]!!.value.split(',')
            program = buildList {
                numbers.forEach {
                    add(it.toInt())
                }

            }
        }
    }
}

fun executeProgram(): List<Int>{
    var totalOutput = mutableListOf<Int>()
    var currentOutput = 0
    while(currentOutput != -1) {
        currentOutput = executeStep()
        if(currentOutput >= 0) {
            totalOutput.add(currentOutput)
        }
    }
    return totalOutput
}

fun executeStep(): Int{
    val instruction =  program.getOrNull(instructionPointer) ?: return -1
    val operand = program.getOrNull(instructionPointer+1) ?: return -1
    var result = -2
    when(instruction){
        0 -> A /= 2.power(combo(operand.toLong()))
        1 -> B = operand.toLong() xor B
        2 -> B = combo(operand.toLong()).mod(8.toLong())
        3 -> if(A != 0.toLong()) {
            instructionPointer = operand - 2
        }
        4 -> B = B xor C
        5 -> result = combo(operand.toLong()).mod(8)
        6 -> B = A / 2.power(combo(operand.toLong())).toLong()
        7 -> C = A / 2.power(combo(operand.toLong())).toLong()
    }
    instructionPointer += 2
    return result
}


fun Int.power(e: Long): Long{
    return if(e == 0.toLong())
        1
    else
        (this * this.power(e-1))
}
fun combo(c : Long): Long{
    require(c in 0..6)
    return when(c.toInt()){
        0 -> 0
        1 -> 1
        2 -> 2
        3 -> 3
        4 -> A
        5 -> B
        else -> C
    }
}
