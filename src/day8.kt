import kotlin.io.path.Path
import kotlin.io.path.readLines

private fun readInput(name: String): MutableList<MutableList<Char>> {
    val input = Path("input/$name.txt").readLines()
    val map: MutableList<MutableList<Char>> = mutableListOf()
    input.forEach{map.add(it.toMutableList())}
    return map
}
val input = readInput("input_day8")
val frequencyToPositions = readAntennaPositions()
val antinodePositions: MutableSet<Pair<Int,Int>> = mutableSetOf()

fun main(){
    computeAllAntinodePositions()
    visualizeAntennasAndAntinodes()
    println("Result: ${antinodePositions.count()}")
}

fun visualizeAntennasAndAntinodes(){
    val adaptedInput = input.toMutableList()
    antinodePositions.forEach {
        adaptedInput[it.first][it.second] = '#'
    }
    adaptedInput.forEach{
        println(it)
    }
}

fun computeAllAntinodePositions() {
   frequencyToPositions.forEach{ c, positions ->
       for(i in positions){
           for(j in positions){
               if(i != j){
                  antinodePositions.addAll(
                      getMoreAntinodes(i, j)
                  )
               }
           }
       }
   }
}

fun getAntinodes(posA: Pair<Int,Int>, posB: Pair<Int,Int>): List<Pair<Int,Int>>{
    val antinodePositions = mutableSetOf<Pair<Int,Int>>()
    antinodePositions.add(posA.subtr(posB.subtr(posA)))
    antinodePositions.add(posB.subtr(posA.subtr(posB)))
    return antinodePositions.filter{
        it.first in 0..<input.count() && it.second in 0..<input.first().count()
    }
}

fun getMoreAntinodes(posA: Pair<Int,Int>, posB: Pair<Int,Int>): List<Pair<Int,Int>> {
    val antinodePostions = mutableSetOf<Pair<Int,Int>>()
    var nextPosFromA = posA
    var nextPosFromB = posB
    while(nextPosFromA.first in 0..<input.count() && nextPosFromA.second in 0..<input.first().count()){
        antinodePostions.add(nextPosFromA)
        nextPosFromA = nextPosFromA.subtr(posB.subtr(posA))
    }
    while(nextPosFromB.first in 0..<input.count() && nextPosFromB.second in 0..<input.first().count()){
        antinodePostions.add(nextPosFromB)
        nextPosFromB = nextPosFromB.subtr(posA.subtr(posB))
    }
    return antinodePostions.toList()
}

fun readAntennaPositions() : MutableMap<Char, MutableSet<Pair<Int,Int>>> {
   val frequencyToPositions = mutableMapOf<Char,MutableSet<Pair<Int,Int>>>()
   input.forEachIndexed{lineIndex, line ->
       line.forEachIndexed{charIndex, char ->
            if (char.isLetterOrDigit()){
                if (char in frequencyToPositions){
                    frequencyToPositions[char]?.add(Pair(lineIndex, charIndex))
                }
                else{
                    frequencyToPositions[char] = mutableSetOf(Pair(lineIndex, charIndex))
                }
            }
       }
   }
    return frequencyToPositions
}