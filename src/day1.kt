import java.io.File
import java.io.InputStream
import kotlin.math.abs

fun main() {
    val inputFileName = "input_day1.txt"
//    solvePartOne(inputFileName)
    solvePartTwo(inputFileName)
}

fun solvePartTwo(inputFileName: String){
    val lists = readLists(inputFileName)
    lists.first.sort()
    lists.second.sort()
    val result = lists.first.fold(0){
        sum, element -> sum + (element*lists.second.count{it == element})
    }
    println("Result: $result")
}

fun solvePartOne(inputFileName: String){
    val tuples = getTuplesOfSortedLists(inputFileName)
    println(tuples)
    val result = tuples.fold(0){
            sum, tuple -> sum + abs(tuple.first - tuple.second)
    }
    println("Result: $result")
}

fun getTuplesOfSortedLists(fileName: String): List<Pair<Int, Int>> {
    val inputLists: Pair<MutableList<Int>, MutableList<Int>> = readLists(fileName)
    inputLists.first.toList()
    inputLists.first.sort()
    inputLists.second.toList()
    inputLists.second.sort()
    return (inputLists.first zip inputLists.second)
}

fun readLists(fileName: String): Pair<MutableList<Int>,MutableList<Int>>{
    val inputStream: InputStream = File(fileName).absoluteFile.inputStream()
    val firstInputList = mutableListOf<Int>()
    val secondInputList = mutableListOf<Int>()
    inputStream.bufferedReader().forEachLine {
        var numbersInLine =  getTuple(it)
        firstInputList.add(numbersInLine.first)
        secondInputList.add(numbersInLine.second)
    }
    return Pair(firstInputList, secondInputList)
}

fun getTuple(input: String): Pair<Int,Int>{
    val values = input.split("   ")
    return Pair(values[0].toInt(), values[1].toInt())
}