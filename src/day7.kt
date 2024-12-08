import kotlin.io.path.Path
import kotlin.io.path.readLines

private fun readInput(name: String)= Path("input/$name.txt").readLines()

fun main(){
    val input = readInput("input_day7")
    var result: Long = 0
    for(line in input){
        val testValue = line.split(':')[0].toLong()
        val remainingNumbers = line.split(": ".toRegex())[1].split(' ').map{it.toInt()}
        if(testValue in getAllPossibleCalibrationsIterative(remainingNumbers)){ // Use .reversed for the recursive version
            result += testValue
        }
    }
    println("Result: $result")
}
fun getAllPossibleCalibrations(calNumbers: List<Int>) : MutableSet<Long> {
    val allCalibrations = mutableSetOf<Long>()
    if(calNumbers.count() == 2) {
        allCalibrations.add((calNumbers[0] + calNumbers[1]).toLong())
        allCalibrations.add((calNumbers[0] * calNumbers[1]).toLong())
        allCalibrations.add((calNumbers[1].toString() + calNumbers[0].toString()).toLong())
    } else {
        val currentElement = calNumbers.first()
        val allPreviousCalibrations = getAllPossibleCalibrations(calNumbers.minusElement(currentElement))
        allPreviousCalibrations.forEach {element ->
            allCalibrations.add(element + currentElement)
            allCalibrations.add(element * currentElement)
            allCalibrations.add((element.toString() + currentElement.toString()).toLong())
        }
    }
    return allCalibrations
}

fun getAllPossibleCalibrationsIterative(calNumbers: List<Int>): MutableSet<Long> {
    var allCalibrations = mutableSetOf<Long>()
    var newCalibrations = mutableSetOf<Long>()
    allCalibrations.add(calNumbers.first().toLong())
    calNumbers.subList(1, calNumbers.count()).forEach{ calNumber ->
        newCalibrations = mutableSetOf<Long>()
        allCalibrations.forEach{ previousCalibration ->
            newCalibrations.add(calNumber + previousCalibration)
            newCalibrations.add(calNumber * previousCalibration)
            newCalibrations.add((previousCalibration.toString() + calNumber.toString()).toLong())
        }
        allCalibrations = newCalibrations
    }
    return allCalibrations
}