package day21

import helpers.*
import kotlin.io.path.Path
import kotlin.io.path.readLines

data class KeyboardNode(
    val x: Int,
    val y: Int,
    val c: Char
): Node

fun main(){
    val numericKeyboard = generateKeyboard("numeric_keyboard")
    val directionalKeyboard = generateKeyboard("directional_keyboard")
    val input = helpers.readInput("input_day21")
    var result = 0
    for(word in input) {
        result += getComplexity(numericKeyboard, directionalKeyboard, word.toMutableList())
    }
    println("Total result: $result")
}

private fun getComplexity(
    numericKeyboard: MutableList<Edge>,
    directionalKeyboard: MutableList<Edge>,
    word: MutableList<Char>
): Int{
    val lengthShortestSequence = getLengthOfShortestTypeSequence(numericKeyboard, directionalKeyboard, word)
    val numericPart = word.getNumericPart()
    val result = lengthShortestSequence * numericPart
    println("Length of shortest sequence: $lengthShortestSequence; numeric part: $numericPart; result: $result")
    return result
}

private fun MutableList<Char>.getNumericPart(): Int{
    val numericPartRegex = "0*(?<number>[1-9][0-9]*)A".toRegex()
    val numericPartMatch = numericPartRegex.matchEntire(this.joinToString(""))
    val numericPart = numericPartMatch!!.groups["number"]!!.value.toInt()
    return numericPart
}

private fun getLengthOfShortestTypeSequence(
    numericKeyboard: MutableList<Edge>,
    directionalKeyboard: MutableList<Edge>,
    word: MutableList<Char>
): Int {
    val shortestSequence = getShortestFullTypeSequence(numericKeyboard, directionalKeyboard, word.toMutableList())
    return shortestSequence.count()
}

private fun getShortestFullTypeSequence(
    numericKeyboard: MutableList<Edge>,
    directionalKeyboard: MutableList<Edge>,
    word: MutableList<Char>
): MutableList<Char> {
    val instructionsRobot1 = howToType(numericKeyboard, word)
    //println("Possible Instructions robot 1: $instructionsRobot1")
    val instructionsRobot2 = buildList {
        instructionsRobot1.forEach {
            add(howToType(directionalKeyboard, it))
        }
    }.flatten()
    //println("Possible Instructions robot 2: $instructionsRobot2")
    val instructionsRobot3 = buildList {
        instructionsRobot2.forEach {
            add(howToType(directionalKeyboard, it))
        }
    }.flatten().sortedBy { it.count() }.first()
    return instructionsRobot3
}

private fun howToType(keyboard: MutableList<Edge>, word: MutableList<Char>): MutableList<MutableList<Char>> {
    val nodes = keyboard.flatMap {
        mutableListOf(it.node1 as KeyboardNode, it.node2 as KeyboardNode)
    }.toSet()
    val nodesToType = word.map{ char ->
        nodes.first { it.c == char}
    }
    //println("Node sequence to type the word $word: $nodesToType")
    var result: MutableList<MutableList<Char>> = mutableListOf(mutableListOf())
    var currentStart = nodes.first { it.c == 'A' }
    for(nextNode in nodesToType){
        val nextPossibleMovements = howToPressNext(currentStart, nextNode, keyboard)
        //println("Moving from $currentStart to $nextNode by pressing $nextMovements")
        result = nextPossibleMovements.flatMap { nextPossibleMovement ->
            result.map { singleCurrentResult ->
                (singleCurrentResult + nextPossibleMovement).toMutableList()
            }
        }.toMutableList()
        currentStart = nextNode
    }
    return result
}

fun howToPressNext(startNode: KeyboardNode, endNode: KeyboardNode, keyboard: MutableList<Edge>): List<MutableList<Char>>{
    val movementSequences = shortestWaysToPressNext(startNode, endNode, keyboard).map { it.asMovementSequence() }
    movementSequences.onEach {
        it.add('A')
    }
    return movementSequences
}

fun shortestWaysToPressNext(
    startNode: KeyboardNode,
    endNode: KeyboardNode,
    keyboard: MutableList<Edge>,
    visitedNodes: MutableSet<KeyboardNode> = mutableSetOf()
): List<MutableList<KeyboardNode>> {
    val allPossibleWaysFromNextNodes: MutableList<MutableList<KeyboardNode>>
    if(startNode == endNode) {
        allPossibleWaysFromNextNodes = mutableListOf(mutableListOf())
    }
    else{
        val nextPossibleNodes = keyboard.filter {
            it.node1 == startNode && !visitedNodes.contains(it.node2)
        }.map { it.node2 }
        allPossibleWaysFromNextNodes = nextPossibleNodes.map {
            val newVisitedNodes = visitedNodes.toMutableSet()
            newVisitedNodes.add(startNode)
            val waysToPressNext =
                shortestWaysToPressNext(it as KeyboardNode, endNode, keyboard, newVisitedNodes).toMutableList()
            waysToPressNext
        }.flatten().toMutableList()
    }
    val allPossibleWays = allPossibleWaysFromNextNodes.onEach { it.add(0, startNode) }
    return allPossibleWays.filter { it.count() == allPossibleWays.map{it.count()}.min() }
}

private fun List<KeyboardNode>.asMovementSequence(): MutableList<Char>{
    var result = ""
    for( (first, second) in this.zipWithNext()){
       val direction = Pair(second.x - first.x, second.y - first.y)
        result += when(direction){
            Pair(1, 0) -> "v"
            Pair(-1, 0) -> "^"
            Pair(0, 1) -> ">"
            else -> "<"
        }
    }
    return result.toMutableList()
}

fun generateKeyboard(resource: String): MutableList<Edge>{
    val description = readInput(resource)
    return createGraph(description)
}

private fun readInput(name: String): MutableList<MutableList<Char>> {
    val input = Path("src/day21/$name.txt").readLines()
    val map: MutableList<MutableList<Char>> = mutableListOf()
    input.forEach{map.add(it.toMutableList())}
    return map
}

private fun createGraph(keyboard: MutableList<MutableList<Char>>): MutableList<Edge> {
    val graph = mutableListOf<Edge>()

    keyboard.forEachIndexed { row, chars ->
        chars.forEachIndexed { column, c ->
            val nodeValue = keyboard[row][column]
            if(nodeValue != '#') {
                for (o in Orientation.entries) {
                    val neighbor = Pair(row + o.x, column + o.y)
                    if (neighbor.onArea(keyboard)) {
                        val neighborValue = keyboard[neighbor.first][neighbor.second]
                        if(neighborValue != '#') graph.add(
                            Edge(
                                KeyboardNode(row, column, nodeValue),
                                KeyboardNode(neighbor.first, neighbor.second, neighborValue),
                                1
                            )
                        )
                    }
                }
            }
        }
    }
    return graph
}
