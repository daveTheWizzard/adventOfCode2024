package day15

import javax.swing.plaf.basic.BasicSplitPaneUI.KeyboardUpLeftHandler
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.*
import helpers.*

enum class Direction(val direction: Pair<Int,Int>){
    UP(Pair(-1,0)),
    DOWN(Pair(1,0)),
    LEFT(Pair(0,-1)),
    RIGHT(Pair(0,1))
}

public var warehouse: MutableList<MutableList<Char>> = mutableListOf()
private var movements: MutableList<Direction> = mutableListOf()
private var robot = Robot(-1 ,-1)


fun main(){
    val input = readInput("input_day15")
    parseInput(input)
    println("Initial state:")
    resizeWarehouse()
    visualize()
    movements.forEachIndexed { index, direction ->
        println()
        println("Move ${direction.name} (step number $index):")
        robot.move(direction)
        //visualize()
    }
    val result = getResult()
    println("Result: $result")
}

fun getResult(): Int{
    var result = 0
    warehouse.forEachIndexed { x, chars ->
        chars.forEachIndexed { y, c ->
            if(c == '[')  result += x*(100) + y
        //result += (100*(min(x, warehouse.lastIndex-x) + min(y, warehouse.first().lastIndex-y-1)))
        }
    }
    return result
}

fun visualize() {
    warehouse.forEach { it.printPretty()}
}

fun MutableList<Char>.printPretty(){
    println(this.foldRight("") { c, acc -> "$c $acc" })
}

private fun readInput(name: String)= Path("input/$name.txt").readLines()

private fun parseInput(input: List<String>){
    warehouse = input.filter { it.contains('#')}.map{it.toMutableList()}.toMutableList()
    movements = input.filter { it.contains("[<>v^]".toRegex()) }.joinToString("").map{
        when(it){
            '^' -> Direction.UP
            'v' -> Direction.DOWN
            '<' -> Direction.LEFT
            else -> Direction.RIGHT
        }
    }.toMutableList()
    val xPos = warehouse.indexOfLast { it.contains('@') }
    val yPos = warehouse[xPos].indexOfLast { it == '@' }
    robot = Robot(xPos, yPos)
}

private fun resizeWarehouse(){
    warehouse = warehouse.map{
        it.flatMap {
            when (it) {
                '#' -> "##"
                'O' -> "[]"
                '.' -> ".."
                else -> "@."
            }.toMutableList()
        }.toMutableList()
    }.toMutableList()
    val xPos = warehouse.indexOfLast { it.contains('@') }
    val yPos = warehouse[xPos].indexOfLast { it == '@' }
    robot = Robot(xPos, yPos)
}

fun Pair<Int,Int>.inWarehouse(): Boolean{
    return (this.first in 0..warehouse.lastIndex && this.second in 0..warehouse.first().lastIndex)
}

fun Pair<Int,Int>.onBorder(): Boolean{
    return (this.first*this.second == 0 || this.first == warehouse.lastIndex || this.second == warehouse.first().lastIndex)
}
