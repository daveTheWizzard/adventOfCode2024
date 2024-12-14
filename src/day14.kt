import kotlin.io.path.Path
import kotlin.io.path.readLines

private fun readInput(name: String)= Path("input/$name.txt").readLines()
val width = 101
val height = 103
val robots: MutableSet<Robot> = mutableSetOf()

fun main(){
    readRobots("input_day14")
    val steps = 100
    robots.forEach { println(it) }
    repeat(10000){
        robots.forEach{ it.move(1)}
        if(robots.count() == (robots.map { it.currentPosition }.toSet()).count()){
            println("Map after ${it+1} steps:")
            visualize()
        }
    }
    //val result = getSafetyFactor()
    //println("Result: $result")
}

fun getSafetyFactor(): Int{
    val q1 = robots.count {
        it.currentPosition.first <= width / 2 - 1 && it.currentPosition.second <= height / 2 - 1
    }
    val q2 = robots.count {
        it.currentPosition.first <= width / 2 - 1 && it.currentPosition.second >= height / 2 + 1
    }
    val q3 = robots.count {
        it.currentPosition.first >= width / 2 + 1 && it.currentPosition.second <= height / 2 - 1
    }
    val q4 = robots.count {
        it.currentPosition.first >= width / 2 + 1 && it.currentPosition.second >= height / 2 + 1
    }
    return q1 * q2 * q3 * q4
}

fun visualize(){
   var map: List<MutableList<Char>> = buildList{
       repeat(height){
           var line: MutableList<Char> = mutableListOf()
           repeat(width) {
               line.add('.')
           }
           add(line)
       }
   }
    map.onEachIndexed { line, chars ->
        chars.onEachIndexed { row, c ->
            val numberOfRobots: Int = robots.count { it.currentPosition.first == row && it.currentPosition.second == line}
            if(numberOfRobots != 0)
                map[line][row] = numberOfRobots.digitToChar()
        }
    }
    map.forEach { println(it) }
}

class Robot(private val initialPosition: Pair<Int,Int>, val velocity: Pair<Int,Int>){
    var currentPosition: Pair<Int,Int> = initialPosition
    override fun toString(): String{
       return "Position: (${currentPosition.first}, ${currentPosition.second}), velocity: (${velocity.first}, ${velocity.second})"
    }

    fun move(steps: Int){
        val x = (currentPosition.first+(steps*velocity.first)).mod(width)
        val y = (currentPosition.second+(steps*velocity.second)).mod(height)
        this.currentPosition = Pair(x,y)
    }
}


fun readRobots(filename: String){
    val input = readInput(filename)
    val regex = "p=(?<x>-?[0-9]+),(?<y>-?[0-9]+) v=(?<v1>-?[0-9]+),(?<v2>-?[0-9]+)".toRegex()
    for(line in input){
        val match = regex.matchEntire(line)!!
        robots.add(
            Robot(
                  Pair(match.groups["x"]!!.value.toInt(), match.groups["y"]!!.value.toInt()),
                  Pair(match.groups["v1"]!!.value.toInt(), match.groups["v2"]!!.value.toInt())
            )
        )
    }
}

