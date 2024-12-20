package day5

import kotlin.io.path.Path
import kotlin.io.path.readLines

private fun readInput(name: String)= Path("input/$name.txt").readLines()

fun main(){
    val input = readInput("input_day5")
    val relations: List<Pair<Int, Int>> = getRelation(input)
    val protocols: List<List<Int>> = getProtocols(input).map{it.split(",").map{it.toInt()}}
    val comparator = ProtocolComparator(relations)
    val resPart1 = part1Day5(comparator, protocols)
    val resPart2 = part2Day5(comparator, protocols)
    println("Result of part 1 is $resPart1 and result of part 2 is $resPart2.")
}


fun part2Day5(comparator: ProtocolComparator, protocols: List<List<Int>>): Int {
   var result = 0
    for (protocol in protocols){
        if(!isSorted(comparator, protocol)){
            val sortedProtocol = protocol.sortedWith(comparator)
            assert(isSorted(comparator, sortedProtocol))
            result += sortedProtocol[sortedProtocol.count() / 2]
        }
    }
    return result
}

fun part1Day5(comparator: ProtocolComparator, protocols: List<List<Int>>): Int {
    var result = 0
    for (protocol in protocols){
        if(isSorted(comparator, protocol))
        result += protocol[protocol.count() / 2]
    }
    return result
}
fun getRelation(input: List<String>) : MutableList<Pair<Int,Int>>{
    val result = mutableListOf<Pair<Int,Int>>()
   for (line in input) {
      if('|' in line) {
          val values = line.split('|')
          result.add(Pair(values[0].toInt(), values[1].toInt()))
      }
   }
    return result
}
fun getProtocols(input: List<String>) : MutableList<String>{
    val result = mutableListOf<String>()
    for (line in input){
        if('|' !in line && line != ""){
           result.add(line)
        }
    }
    return result
}

fun isSorted(comparator: ProtocolComparator, protocolList: List<Int>) : Boolean{
    val comparisons = protocolList.zipWithNext{a,b -> comparator.compare(a,b)}
    return (1 !in comparisons)
}