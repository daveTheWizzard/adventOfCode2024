import org.w3c.dom.ranges.Range
import java.awt.font.NumericShaper
import kotlin.io.path.Path
import kotlin.io.path.readLines

private fun readInput(name: String)= Path("input/$name.txt").readLines()

fun main(){
    var input:String = readInput("input_day3").toString()
    val resPartOne = partOne(input)
    val resPartTwo = partTwo(input)
    println("Result of Part 1: $resPartOne, result of Part 2: $resPartTwo")
}

fun partTwo(input: String): Int{
    val regexDont = Regex("don't\\(\\)")
    val regexDo = Regex("do\\(\\)")
    var intervalStart = 0
    var intervalEnd = 0
    var intervalValue = 0
    var res = 0
    while(intervalStart < input.length-1) {
        intervalEnd = regexDont.find(input, intervalStart)?.range?.first ?: (input.length-1)
        //println("Current interval from $intervalStart to $intervalEnd is ${input.substring(intervalStart..intervalEnd)}")
        intervalValue = partOne(input.substring(intervalStart..intervalEnd))
        //println("Interval value: $intervalValue")
        res += intervalValue
        intervalStart = regexDo.find(input,intervalEnd)?.range?.last ?: input.length
    }
    return res
}

fun partOne(input: String): Int{
    val regex = Regex("mul\\([0-9]+,[0-9]+\\)")
    val matches = regex.findAll(input)
    val mults = matches.map{it.value}
    val res = mults.fold(0, {acc, mult -> acc + getProduct(mult)})
    return res
}
fun getProduct(mult: String):Int{
    val matches = Regex("[0-9]+").findAll(mult)
    val numbers = matches.map{it.value.toInt()}
    return numbers.fold(1, {acc, element -> acc * element})
}