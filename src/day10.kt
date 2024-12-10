import kotlin.io.path.Path
import kotlin.io.path.readLines


private fun readInput(name: String): MutableList<MutableList<Int>> {
    val input = Path("input/$name.txt").readLines()
    val map: MutableList<MutableList<Int>> = mutableListOf()
    input.forEach{map.add(it.toMutableList().map { it.digitToInt() }.toMutableList())}
    return map
}

val inputD10: MutableList<MutableList<Int>> = readInput("input_day10")

fun main(){
    val totalScore = getTotalScore()
    println("Total score: $totalScore")
}

fun getTotalScore(): Int {
    var totalScore = 0
    inputD10.forEachIndexed { rowIndex, line ->
        line.forEachIndexed { columnIndex, v ->
            val pos = Pair(rowIndex, columnIndex)
            if(inputD10[rowIndex][columnIndex] == 0) {
                val score = getNumberOfPathsForStartpoint(pos) // Change to val score = getScoreOfTrailheadPart1(pos) for part 2
                //println("Total score of $rowIndex,$columnIndex is $score.")
                totalScore += score
            }
        }
    }
    return totalScore
}

fun getScoreOfTrailheadPart1(trailhead: Pair<Int, Int>): Int{
    val reachableTops = getAllReachableTopsForTrailhead(trailhead)
    return reachableTops.count()
}

fun getNumberOfPathsForStartpoint(trailhead: Pair<Int, Int>): Int{
    if(inputD10[trailhead.first][trailhead.second] == 9)
        return 1
    val nextPossiblePositions = getNextPositions(trailhead)
    var result = 0
    for (p in nextPossiblePositions){
        result += getNumberOfPathsForStartpoint(p)
    }
    return result
}

fun getAllReachableTopsForTrailhead(trailhead: Pair<Int,Int>): MutableSet<Pair<Int,Int>>{
    require(inputD10[trailhead.first][trailhead.second] == 0)
    val tops = mutableSetOf<Pair<Int,Int>>()
    var reachablePositions = mutableSetOf(trailhead)
    while(reachablePositions.isNotEmpty()){
           val currentPosition = reachablePositions.first()
           val nextPositions = getNextPositions(currentPosition)
           val foundTops = nextPositions.filter { inputD10[it.first][it.second] == 9 }.toMutableSet()
           tops.addAll(foundTops)
           reachablePositions.remove(currentPosition)
           reachablePositions.addAll(nextPositions.filter { inputD10[it.first][it.second] != 9 }.toMutableSet())
    }
    return tops
}

private fun Pair<Int,Int>.inRange(): Boolean{
    return(this.first in 0..inputD10.lastIndex && this.second in 0..inputD10[0].lastIndex)
}

fun getNextPositions(pos: Pair<Int,Int>): MutableSet<Pair<Int,Int>>{
    val directions = mutableSetOf(Pair(-1,0), Pair(1,0), Pair(0,1), Pair(0,-1))
    val result = mutableSetOf<Pair<Int,Int>>()
    for(d in directions){
        val nextPos = pos.add(d)
        if(nextPos.inRange() && inputD10[nextPos.first][nextPos.second] == inputD10[pos.first][pos.second] + 1){
           result.add(nextPos)
        }
    }
    return result
}