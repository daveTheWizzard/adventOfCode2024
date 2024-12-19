import helpers.*

var patterns: List<String> = listOf()
var designs: List<String> = listOf()
val numberOfArrangements: MutableMap<String, Long> = mutableMapOf()

fun main(){
    val input = readInput("input_day19")
    parseInput(input)
    val result = designs.map{
        val c = countArrangements(it)
        println("Number of possible arrangements for pattern $it: $c.")
        c.toLong()
    }.sum()
    println("Number of matches: $result")
}

private fun parseInput(input: List<String>){
    patterns = input.first().split(", ")
    designs = input.subList(2, input.count())
}

private fun countArrangements(design: String): Long {
    var numberArrangements: Long = 0
    if(design == "")
        return 1
    if(numberOfArrangements.keys.contains(design))
        return numberOfArrangements[design]!!
    patterns.forEach {
        if (design.startsWith(it)) {
            numberArrangements += countArrangements(design.substring(it.count(), design.count()))
            numberOfArrangements[design] = numberArrangements
        }
    }
    return numberArrangements
}