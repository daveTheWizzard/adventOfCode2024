import helpers.*

var patterns: List<String> = listOf()
var designs: List<String> = listOf()
var matchable: MutableSet<String> = mutableSetOf()
var notMatchable: MutableSet<String> = mutableSetOf()

fun main(){
    val input = readInput("input_day19")
    parseInput(input)
    val result = designs.map{
        println("Considered pattern $it.")
        matchDesign(it)
    }.count { it }
    println("Number of matches: $result")
}

private fun parseInput(input: List<String>){
    patterns = input.first().split(", ")
    designs = input.subList(2, input.count())
}

private fun getNumberOfPossibleMatches(design: String): Int{
    return 0
}
private fun matchDesign(design: String): Boolean {
    //println("Trying to match $design")
    if(matchable.contains(design))
        return true
    if(notMatchable.contains(design))
        return false
    //println()
    //println("Considering design $design")
    if (patterns.map { it == design }.contains(true)) {
        //println("The design $design is equal to the pattern ${patterns.filter{it == design}}")
        return true
    }
    if (patterns.map { design.startsWith(it) }.contains(true)) {
        //println("Design starts with the patterns ${patterns.filter{design.startsWith(it)}}")
        //println("Working with substrings ${patterns.filter{design.startsWith(it)}.map{design.substring(it.count(), design.count())}}.")
    }
    patterns.forEach {
        if(it.count() <= design.count()) {
            val substring = design.substring(it.count(), design.count())
            if (design.startsWith(it) && matchDesign(substring)) {
                matchable.add(design)
                return true
            }
        }
    }
    notMatchable.add(design)
    return false
}