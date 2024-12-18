import kotlin.io.path.Path
import kotlin.io.path.readLines
import helpers.*

var bytePositions: List<Pair<Int, Int>> = listOf()
var memorySpace: List<MutableList<Char>> = listOf()
var bytesToConsider: Int = 1024
val coordinateRange = 70

class MemoryNode(val x: Int, val y: Int): Node

fun main(){
   val input = readInput("input_day18")
    parseInput(input)
    constructMemorySpace()
    val graph: List<Edge> = buildGraph()
    val result = findShortestPath(graph, MemoryNode(0,0), MemoryNode(coordinateRange, coordinateRange))
    val shortesPath = result.shortestPath()
    val shortestDistance =  result.shortestDistance() ?: 1
    while(shortestDistance > 0) {
        //To do
    }
}

private fun readInput(name: String)= Path("input/$name.txt").readLines()

private fun parseInput(input: List<String>){
    bytePositions = buildList {
        input.forEach{
            val numbers = it.split(',')
            require(numbers.size == 2)
            add(Pair(numbers[0].toInt(), numbers[1].toInt()))
        }
    }
}

private fun constructMemorySpace(){
    memorySpace = MutableList(coordinateRange+1) {MutableList(coordinateRange+1) {'.'} }
    for(i in 0.rangeTo(bytesToConsider-1)){
        memorySpace[bytePositions[i].second][bytePositions[i].first] = '#'
    }
}

private fun buildGraph(): List<Edge>{
    val graph = mutableListOf<Edge>()
    memorySpace.forEachIndexed { row, chars ->
        chars.forEachIndexed { column, c ->
            if(c != '#'){
                for(o in Orientation.entries){
                    val neighbor = Pair(column + o.y, row + o.x)
                    if(neighbor.inMemory() && memorySpace[neighbor.second][neighbor.first] != '#') {
                        graph.add(Edge(MemoryNode(column, row), MemoryNode(neighbor.first, neighbor.second), 1))
                        println("Added edge from ${Pair(column, row)} to ${Pair(neighbor.first, neighbor.second)}")
                    }
                }
            }
        }
    }
    return graph
}

private fun Pair<Int,Int>.inMemory(): Boolean{
    return (this.first in 0..memorySpace.first().lastIndex && this.second in 0..memorySpace.lastIndex)
}

private fun visualize(nodes: List<MemoryNode?>){
    var memoryCopy = memorySpace.toMutableList()
    nodes.forEach {
        if (it != null) {
            memoryCopy[it.y][it.x]  = 'O'
        }
    }
    memoryCopy.forEach {
        println(it)
    }
}