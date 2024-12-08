import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.abs

private fun readInput(name: String)= Path("input/$name.txt").readLines().toMutableList()

fun main(){
    var input =  readInput("input_day2")
    var stringList = input.map{it.split(" ")}
    var numberList = stringList.map{it.map{it.toInt()}}
    println(numberList.map{dampedReportIsSafe(it.toMutableList())}.count{it})
}

fun reportIsSafe(report: MutableList<Int>): Boolean{
    val res = report.zipWithNext{a, b -> a- b}
    return res.all{abs(it) in 1..3} && (res.all{it < 0} || res.all{it > 0})
}

fun dampedReportIsSafe(report: List<Int>): Boolean{
    var res = reportIsSafe(report.toMutableList())
    report.forEachIndexed { index, _ ->
       val workingReport = report.toMutableList().apply{ removeAt(index)}
       res = res || reportIsSafe(workingReport)
    }
    return res
}
