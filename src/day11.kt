import kotlin.io.path.Path
import kotlin.io.path.readLines

private fun readInput(name: String)= Path("input/$name.txt").readLines().first()

val initialStones: List<ULong> = readStones("input_day11")
var computedStoneList = listOf<Pair<ULong,ULong>>()

fun readStones(filename: String): List<ULong>{
    val input: String = readInput(filename)
    return input.split(' ').map {
        it.toULong()
    }
}

fun main(){
    val numberOfBlinks = 75
    computeBlinks(numberOfBlinks)
    val result = computedStoneList.fold(
        0.toULong(), {acc, element -> acc + element.second.toULong()}
    )
    println("Number of stones after $numberOfBlinks blinks: $result")
}
fun computeBlinks(numberBlinks: Int){
    computedStoneList = initialStones.map { Pair(it, 1.toULong()) }
    println("Initital arrangement:")
    println(computedStoneList)
    for(i in 0..<numberBlinks){
        computeListAfterBlink()
        //println("After ${i+1} blinks:")
        //println(computedStoneList)
        reduceList()
        //println("Reduced list:")
        //println(computedStoneList)
    }
}
fun reduceList(){
    val grouping = computedStoneList.groupingBy {
        it.first
    }
    val groupValues = grouping.aggregate { key, accumulator: Pair<ULong, ULong>?, element, first ->
        if(first)
            element
        else
            Pair(key, accumulator!!.second + element.second)
    }
    computedStoneList = groupValues.values.toList()
}

fun computeListAfterBlink(){ //Might hav
    var newStoneList = listOf<Pair<ULong,ULong>>()
    newStoneList = computedStoneList.flatMap { stone ->
       when{
           stone.first == 0.toULong() -> listOf(Pair(1.toULong(), stone.second))
           (stone.first.numberOfDigits() % 2.toULong()) == 0.toULong() -> stone.split()
           else -> listOf(Pair(stone.first * 2024.toULong(), stone.second))
       }
    }
    computedStoneList = newStoneList
}

fun Pair<ULong,ULong>.split(): List<Pair<ULong,ULong>> {
    require(this.first.numberOfDigits() % 2.toULong() == 0.toULong())
    val numberAsString = this.first.toString()
    val part1 = numberAsString.substring(0, numberAsString.length/2)
    val part2 = numberAsString.substring(numberAsString.length/2)
    return listOf(Pair(part1.toULong(),this.second), Pair(part2.toULong(), this.second))
}
fun ULong.numberOfDigits(): ULong{
    return when{
        (this <= 9.toULong()) -> 1.toULong()
        else -> 1.toULong() + (this/10.toULong()).numberOfDigits()
    }
}