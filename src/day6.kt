import javax.swing.text.Position import kotlin.io.path.Path import kotlin.io.path.readLines

private fun readInput(name: String): MutableList<MutableList<Char>> {
    val input = Path("input/$name.txt").readLines()
    val map: MutableList<MutableList<Char>> = mutableListOf()
    input.forEach{map.add(it.toMutableList())}
    return map
}

enum class Direction{
    UP, DOWN, LEFT, RIGHT
}

fun getFirstPosition() : Pair<Int,Int>{
    val xStartPosition = map.indexOf(map.find{it.contains('^')})
    val yStartPosition = map[xStartPosition].indexOf('^')
    assert(xStartPosition != -1 && yStartPosition != -1)
    return Pair(xStartPosition, yStartPosition)
}

fun getFirstDirection() : Direction{
    assert(map[startPosition.first][startPosition.second] in listOf('v', '^', '<', '>'))
    return when(map[startPosition.first][startPosition.second]) {
        '<' -> Direction.LEFT
        '>' -> Direction.RIGHT
        '^' -> Direction.UP
        else -> Direction.DOWN
    }
}
var map: MutableList<MutableList<Char>> = readInput("input_day6")
var startPosition: Pair<Int, Int> = getFirstPosition()
var currentPosition: Pair<Int, Int> = startPosition
var startDirection: Direction = getFirstDirection()
var currentDirection: Direction = startDirection
var visitedPositions : MutableSet<Pair<Pair<Int,Int>, Direction>> = mutableSetOf(Pair(startPosition, currentDirection))

fun main(){
    partOne()
    partTwo()
}

fun partTwo(){
    var res = 0
   for (x in 0..<map.count()) {
       for (y in 0..<map[0].count()) {
           currentPosition = startPosition
           currentDirection = startDirection
           visitedPositions = mutableSetOf()
           if (map[x][y] != '#' && Pair(x, y) != startPosition) {
               map[x][y] = '#'
               var foundCircle = false
               while (!foundCircle) {
                   visitedPositions.add(Pair(currentPosition, currentDirection))
                   if(!moveGuard()){
                       break
                   }
                   if (Pair(currentPosition, currentDirection) in visitedPositions) {
                       foundCircle = true
                       res += 1
                   }
                   //visualizeDirectionalStatus()
               }
               map[x][y] = '.'
           }
       }
   }
    println("Found $res circles!")
}


fun isHorizontal(d : Direction) : Boolean {
    return (d == Direction.LEFT || d == Direction.RIGHT)
}

fun circleDetected() : Boolean {
    val groupedByPosition = visitedPositions.groupingBy { Pair(it.first, isHorizontal(it.second))}.eachCount()
    return groupedByPosition.any { (a,b) -> b > 3 }
}

fun partOne(){
    println("Let's go!")
    while (moveGuard()){
        // visualizeStatus()
        if(visitedPositions.none { (pos, dir) -> pos == currentPosition})
            visitedPositions.add(Pair(currentPosition, currentDirection))
    }
    println("Visited ${visitedPositions.count()} distinct positions!")
}

fun visualizeStatus(){
    val currentMap: MutableList<MutableList<Char>> = mutableListOf()
    map.forEach{currentMap.add(it.toMutableList())}
    visitedPositions.forEach{(pos, direction) -> currentMap[pos.first][pos.second] = 'X'}
    currentMap[currentPosition.first][currentPosition.second] = when(currentDirection){
        Direction.UP -> '^'
        Direction.DOWN -> 'v'
        Direction.LEFT -> '<'
        Direction.RIGHT -> '>'
    }
    println()
    currentMap.forEach{println(it)}
}

fun visualizeDirectionalStatus(){
    val currentMap: MutableList<MutableList<Char>> = map.toMutableList()
    visitedPositions.forEach{ (position, direction) ->
       if (isHorizontal(direction)) {
           if (currentMap[position.first][position.second] == '|') {
               map[position.first][position.second] = '+'
           } else {
               map[position.first][position.second] = '-'
           }
       } else {
           if (currentMap[position.first][position.second] == '-') {
               map[position.first][position.second] = '+'
           } else {
               map[position.first][position.second] = '|'
           }
       }
    }
    println()
    currentMap.forEach{println(it)}
}

fun moveGuard(): Boolean {
    val turnTo = mapOf(
        Direction.UP to Direction.RIGHT,
        Direction.RIGHT to Direction.DOWN,
        Direction.DOWN to Direction.LEFT,
        Direction.LEFT to Direction.UP
    )
    val stepTo = mapOf(
        Direction.UP to Pair(-1, 0),
        Direction.RIGHT to Pair(0, 1),
        Direction.DOWN to Pair(1, 0),
        Direction.LEFT to Pair(0, -1)
    )
    var positionOfObjectInFront = currentPosition.add(stepTo[currentDirection]!!)
    if (positionOfObjectInFront.first !in 0..map.count() - 1 || positionOfObjectInFront.second !in 0..map.first().count() - 1) {
        return false
    }
    else{
       if(map[positionOfObjectInFront.first][positionOfObjectInFront.second] == '#'){
           currentDirection = turnTo[currentDirection]!!
       } else {
           currentPosition = positionOfObjectInFront
       }
        return true
    }
}