import helpers.*
import kotlin.io.path.Path
import kotlin.io.path.readLines

data class MazeNode(
    override val x: Int,
    override val y: Int,
    override val o: Orientation
): Node

var startTile: Pair<Int,Int> = Pair(-1,-1)
var endTile: Pair<Int,Int> = Pair(-1,-1)
var maze: MutableList<MutableList<Char>> = mutableListOf()
// var graph: List<Edge> = createGraph()

fun main(){
    maze = readInput("input_day16_example")
    findStartandEnd()
    val graph = createGraph()
    val result1 = findShortestPath(graph, MazeNode(startTile.first, startTile.second, Orientation.EAST), MazeNode(endTile.first, endTile.second, Orientation.NORTH))
    val result2 = findShortestPath(graph, MazeNode(startTile.first, startTile.second, Orientation.EAST), MazeNode(endTile.first, endTile.second, Orientation.EAST))
    var result: ShortestPathResult
    if(result1.shortestDistance()!! < result2.shortestDistance()!!)
        result = result1
    else
        result = result2
    println("prev: ${result.prev}")
    println("dist: ${result.dist}")
    println("Shortest path:")
    val shortestPath = result.shortestPath()
    println(shortestPath)
    println("Length of shortest path: ${shortestPath.map{Pair(it!!.x, it!!.y)}.size}")
    println()
    visualize(shortestPath)
    println(result.shortestDistance())
    //   println(graph)
}
private fun visualize(nodes: List<Node?>){
   var mazeCopy = maze.toMutableList()
   nodes.forEach {
       var orientation = when(it?.o){
          Orientation.EAST -> '>'
          Orientation.NORTH -> '^'
           Orientation.SOUTH -> 'v'
           else -> '<'
       }
       if (it != null) {
           mazeCopy[it.x][it.y]  = orientation
       }
   }
    mazeCopy.forEach {
        println(it)
    }
}

private fun readInput(name: String): MutableList<MutableList<Char>> {
    val input = Path("input/$name.txt").readLines()
    val map: MutableList<MutableList<Char>> = mutableListOf()
    input.forEach{map.add(it.toMutableList())}
    return map
}

private fun findStartandEnd(){
    val start = 'S'
    val end = 'E'
    val xStart = maze.indexOfLast { it.contains(start) }
    val yStart = maze[xStart].indexOfLast { it == start }
    val xEnd = maze.indexOfLast { it.contains(end) }
    val yEnd = maze[xEnd].indexOfLast { it == end}
    startTile = Pair(xStart, yStart)
    endTile = Pair(xEnd, yEnd)
}

enum class Orientation(val x: Int, val y: Int){
    NORTH(-1,0),
    SOUTH(1,0),
    WEST(0,-1),
    EAST(0,1)
}
private fun createGraph(): MutableList<Edge> {
    val graph = mutableListOf<Edge>()

    maze.forEachIndexed { row, chars ->
        chars.forEachIndexed { column, c ->
            if(c != '#') {
                // Transfers
                for(o in Orientation.entries){
                    val neighbor = Pair(row + o.x, column + o.y)
                    if(neighbor.inMaze() && maze[neighbor.first][neighbor.second] != '#')
                        graph.add(Edge(MazeNode(row, column, o), MazeNode(neighbor.first, neighbor.second, o), 1))
                }
                // Turns
                val turnDistance = 1000
                graph.addAll(
                    listOf(
                        Edge(MazeNode(row, column, Orientation.NORTH), MazeNode(row, column, Orientation.EAST), turnDistance),
                        Edge(MazeNode(row, column, Orientation.EAST), MazeNode(row, column, Orientation.SOUTH), turnDistance),
                        Edge(MazeNode(row, column, Orientation.SOUTH), MazeNode(row, column, Orientation.WEST), turnDistance),
                        Edge(MazeNode(row, column, Orientation.WEST), MazeNode(row, column, Orientation.NORTH), turnDistance),
                        Edge(MazeNode(row, column, Orientation.NORTH), MazeNode(row, column, Orientation.WEST), turnDistance),
                        Edge(MazeNode(row, column, Orientation.WEST), MazeNode(row, column, Orientation.SOUTH), turnDistance),
                        Edge(MazeNode(row, column, Orientation.SOUTH), MazeNode(row, column, Orientation.EAST), turnDistance),
                        Edge(MazeNode(row, column, Orientation.EAST), MazeNode(row, column, Orientation.NORTH), turnDistance),
                    )
                )
            }
        }
    }
    return graph
}

fun Pair<Int,Int>.inMaze(): Boolean{
    return (this.first in 0..maze.lastIndex && this.second in 0..maze.first().lastIndex)

}

/*data class PairNode(val x: Int, val y: Int): Node
fun example(){
    val graph = listOf(
        Edge(PairNode(1,2), PairNode(2,3), 4),
        Edge(PairNode(2,3), PairNode(3,4), 4)
    )
    val result = findShortestPath(graph, PairNode(1,2), PairNode(3,4))
    println("prev: ${result.prev}")
    println("dist: ${result.dist}")
    println(result.shortestPath())
    println(result.shortestDistance())
}*/