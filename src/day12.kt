/*import kotlin.io.path.Path
import kotlin.io.path.readLines

var regions: MutableSet<Set<Pair<Int,Int>>> = mutableSetOf()
val garden = readInput("input_day12_part2_e2")

private fun readInput(name: String): MutableList<MutableList<Char>> {
    val input = Path("input/$name.txt").readLines()
    val map: MutableList<MutableList<Char>> = mutableListOf()
    input.forEach{map.add(it.toMutableList())}
    return map
}

fun main(){
    println("Input: $garden")
    computeAllRegions()
    var totalPrice: Long = 0
    for(r in regions) {
        println("   Region: $r with chars ${garden[r.first().first][r.first().second]}")
        val regionPrice = getPrice(r)
        totalPrice += regionPrice
        println("   Price: ${regionPrice}")
        println()
    }
    println("Total price: $totalPrice")
}


fun getPrice(region: Set<Pair<Int,Int>>): Int{
    val perimeter = getNumberOfSides(region)
    val area = region.count()
    return area*perimeter
}
fun getPerimeter(region: Set<Pair<Int,Int>>): Int{
    val directions = mutableSetOf(Pair(-1,0), Pair(1,0), Pair(0,1), Pair(0,-1))
    val charOfRegion = garden[region.first().first][region.first().second]
    var perimeter = 0
    for(e in region) {
        for (d in directions) {
            val neighbor = e.add(d)
            if (!neighbor.inGarden() || garden[neighbor.first][neighbor.second] != charOfRegion) perimeter++
        }
    }
    return perimeter
}

/*fun getNumberOfSides2(region: Set<Pair<Int,Int>>): Int {
    var numberOfSides = 0
    val topSides = region.filter {
        !region.contains(it.add(top))
    }.groupBy {  }
    println("      Number of top sides: $topSides")
    val bottomSides = region.filter {
        !region.contains(it.add(bottom))
    }.map {it.first}.distinct().count()
    println("      Number of bottom sides: $bottomSides")
    val leftSides = region.filter {
        !region.contains(it.add(left))
    }.map {it.second}.distinct().count()
    println("      Number of left sides: $leftSides")
    val rightSides = region.filter {
        !region.contains(it.add(right))
    }.map {it.second}.distinct().count()
    println("      Number of right sides: $rightSides")
    return topSides + bottomSides + rightSides + leftSides
}*/

val right = Pair(0,1)
val left = Pair(0,-1)
val top = Pair(-1,0)
val bottom = Pair(1,0)

/*fun getNumberOfSides(region: Set<Pair<Int,Int>>): Int{
   var borderNodes = region.filter{
       region.contains(it.add(top)) || region.contains(it.add(bottom)) ||region.contains(it.add(left)) ||region.contains(it.add(right))
   }
    while(!borderNodes.isEmpty()){
        val currentNode = borderNodes.first()

    }
}
fun getNumberOfSidesFromNode(region: Set<Pair<Int, Int>>): Pair<Int,Set<Pair<Int,Int>>>{
    var visitedNodes: Set<Pair<Int,Int>> = setOf()
    var numberOfSides = 0
    val startNode = region.first {
        it.first == region.minOf { it.first }
    }
    visitedNodes = visitedNodes.plus(startNode)
    var currentNode = startNode
    var nextNode: Pair<Int, Int>? = null
    var startDirectionOfOutside = top
    var currentDirection: Pair<Int,Int> = startDirectionOfOutside
    var nextDirection: Pair<Int, Int>? = null
    while(nextNode != startNode || nextDirection != startDirectionOfOutside ) {
        if (currentDirection == top) {
            if(region.contains(currentNode.add(top.add(right))) && region.contains(currentNode.add(right))){
                nextNode = currentNode.add(top.add(right))
                nextDirection = left
                numberOfSides++
            } else if (region.contains(currentNode.add(right))) {
                nextNode = currentNode.add(right)
                nextDirection = currentDirection
            } else {
                nextDirection = right
                nextNode = currentNode
                numberOfSides++
            }
        } else if (currentDirection == right) {
            if (region.contains(currentNode.add(right.add(bottom))) && region.contains(currentNode.add(bottom))) {
                nextNode = currentNode.add(right.add(bottom))
                nextDirection = top
                numberOfSides++
            } else if (region.contains(currentNode.add(bottom))) {
                nextNode = currentNode.add(bottom)
                nextDirection = currentDirection
            } else {
                nextNode = currentNode
                nextDirection = bottom
                numberOfSides++
            }
        } else if (currentDirection == bottom) {
            if (region.contains(currentNode.add(bottom.add(left))) && region.contains(currentNode.add(left))) {
                nextNode = currentNode.add(bottom.add(left))
                nextDirection = right
                numberOfSides++
            } else if (region.contains(currentNode.add(left))) {
                nextNode = currentNode.add(left)
                nextDirection = currentDirection
            } else {
                nextDirection = left
                nextNode = currentNode
                numberOfSides++
            }
        } else if (currentDirection == left) {
            if (region.contains(currentNode.add(left.add(top))) && region.contains(currentNode.add(top))) {
                nextNode = currentNode.add(left.add(top))
                nextDirection = bottom
                numberOfSides++
            } else if (region.contains(currentNode.add(top))) {
                nextNode = currentNode.add(top)
                nextDirection = currentDirection
            }  else {
                nextDirection = top
                nextNode = currentNode
                numberOfSides++
            }
        }
        require(nextNode != null) {"Next node is null!"}
        require(nextDirection != null){"Next direction is null!"}
        currentNode = nextNode
        visitedNodes = visitedNodes.plus(currentNode)
        currentDirection = nextDirection
    }
    return Pair(numberOfSides,visitedNodes)
}*/

fun computeAllRegions(){
    for(x in 0..garden.lastIndex){
        for(y in 0..garden.first().lastIndex){
            computeRegionStartingFrom(x,y)
        }
    }
}

fun computeRegionStartingFrom(x: Int, y: Int){
    var extendedRegion = setOf(Pair(x,y))
    var currentRegion: Set<Pair<Int,Int>> = setOf()
    if(notContainedInAnyRegion(x,y)){
        while(currentRegion != extendedRegion) {
            currentRegion = extendedRegion
            extendedRegion = extendRegion(currentRegion)
        }
        regions.add(extendedRegion)
    }
}

fun extendRegion(currentRegion: Set<Pair<Int, Int>>): Set<Pair<Int,Int>>{
    val charOfRegion = garden[currentRegion.first().first][currentRegion.first().second]
    val newPositions = mutableSetOf<Pair<Int,Int>>()
    val directions = mutableSetOf(Pair(-1,0), Pair(1,0), Pair(0,1), Pair(0,-1))
    for(pos in currentRegion){
        for(d in directions){
            val newCandidate = pos.add(d)
            if(newCandidate.inGarden() && garden[newCandidate.first][newCandidate.second] == charOfRegion)
                newPositions.add(newCandidate)
        }
    }
    return newPositions.union(currentRegion)
}

fun Pair<Int,Int>.inGarden(): Boolean{
    return (this.first in 0..garden.lastIndex && this.second in 0..garden.first().lastIndex)
}

fun notContainedInAnyRegion(x: Int, y: Int): Boolean{
   val numberOfRegions = regions.count { it.contains(Pair(x, y)) }
    require(numberOfRegions <= 1)
    return numberOfRegions == 0
}
*/