import helpers.*
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.*

data class ProgramNode(
    val x: Int,
    val y: Int,
): Node

private var startNode: Pair<Int,Int> = Pair(-1,-1)
private var endNode: Pair<Int,Int> = Pair(-1,-1)
private var program: MutableList<MutableList<Char>> = mutableListOf()
// var graph: List<Edge> = createGraph()

fun main(){
    program = readInput("input_day20")
    findStartAndEnd()
    val graph = createGraph()
    val result = findShortestPath(graph, ProgramNode(startNode.first, startNode.second), ProgramNode(endNode.first, endNode.second))
    val shortestDistance = result.shortestDistance()
    val shortestPath: List<ProgramNode> = result.shortestPathIterative() as List<ProgramNode>
    val hashNodes = buildList {
        program.forEachIndexed { row, chars ->
            chars.forEachIndexed { column, c ->
                if(program[row][column] == '#')
                    add(ProgramNode(row, column))
            }
        }
    }
    val cheats = buildList {
        hashNodes.forEach {
            val newDistance = getImprovedPathLength(it, result.dist as Map<ProgramNode, Int>, shortestDistance!!)
            if (newDistance != Int.MAX_VALUE)
                add(shortestDistance - newDistance)
        }
    }
    val cheatCounts = cheats.groupingBy { it }.eachCount()
    var goodCheatsCount = cheats.count { it >= 100 }
    println(goodCheatsCount)
}

private fun getImprovedPathLength(
    nodeToRemove: ProgramNode,
    dist: Map<ProgramNode, Int>,
    pathLength: Int
): Int{
    if(!nodeToRemove.isCandidate())
        return Int.MAX_VALUE
    var neighborA: ProgramNode
    var neighborB: ProgramNode
    if(nodeToRemove.UpAndDownFree()){
       neighborA = ProgramNode(nodeToRemove.x - 1, nodeToRemove.y)
       neighborB = ProgramNode(nodeToRemove.x + 1, nodeToRemove.y)
    } else{ // Left and right is free
       neighborA = ProgramNode(nodeToRemove.x, nodeToRemove.y - 1)
       neighborB = ProgramNode(nodeToRemove.x, nodeToRemove.y + 1)
    }
    //println("   Remove neighbor $neighborA with distance of ${dist[neighborA]}")
    //println("   Remove neighbor $neighborB with distance of ${dist[neighborB]}")
    return min(dist[neighborA]!!, dist[neighborB]!!) + (pathLength - max(dist[neighborA]!!, dist[neighborB]!!)) + 2
}

private fun ProgramNode.isCandidate(): Boolean{
    return if(this.onBorder())
        false
    else
        this.UpAndDownFree() || this.LeftAndRightFree()
}

private fun ProgramNode.onBorder(): Boolean{
    return (this.x == 0 || this.x == program.lastIndex || this.y == 0 || this.y == program.first().lastIndex)
}

private fun ProgramNode.UpAndDownFree(): Boolean{
    return program[this.x - 1][this.y] != '#' && program[this.x + 1][y] != '#'
}

private fun ProgramNode.LeftAndRightFree(): Boolean{
    return program[this.x][this.y - 1] != '#' && program[this.x][this.y + 1] != '#'
}

private fun visualize(nodes: List<ProgramNode?>){
    val programCopy = program.toMutableList()
    nodes.forEach {
        if (it != null) {
            programCopy[it.x][it.y]  = 'O'
        }
    }
    programCopy.forEach {
        println(it)
    }
}

private fun readInput(name: String): MutableList<MutableList<Char>> {
    val input = Path("input/$name.txt").readLines()
    val map: MutableList<MutableList<Char>> = mutableListOf()
    input.forEach{map.add(it.toMutableList())}
    return map
}

private fun findStartAndEnd(){
    val start = 'S'
    val end = 'E'
    val xStart = program.indexOfLast { it.contains(start) }
    val yStart = program[xStart].indexOfLast { it == start }
    val xEnd = program.indexOfLast { it.contains(end) }
    val yEnd = program[xEnd].indexOfLast { it == end}
    startNode = Pair(xStart, yStart)
    endNode = Pair(xEnd, yEnd)
}

private fun createGraph(): MutableList<Edge> {
    val graph = mutableListOf<Edge>()

    program.forEachIndexed { row, chars ->
        chars.forEachIndexed { column, c ->
            if(program[row][column] != '#') {
                for (o in Orientation.entries) {
                    val neighbor = Pair(row + o.x, column + o.y)
                    if (neighbor.inProgram() && program[neighbor.first][neighbor.second] != '#')
                        graph.add(Edge(ProgramNode(row, column), ProgramNode(neighbor.first, neighbor.second), 1))
                }
            }
        }
    }
    return graph
}

private fun Pair<Int,Int>.inProgram(): Boolean{
    return (this.first in 0..program.lastIndex && this.second in 0..program.first().lastIndex)
}