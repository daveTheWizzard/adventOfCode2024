import helpers.power
import kotlin.math.pow

data class Variable(var name: String, var v: Boolean?){
    override fun toString(): String {
        return """$name: ${if (v != null) v else "unassigned"}"""
    }
}

class VariableComparator() : Comparator<Variable> {
    override fun compare(a: Variable,  b: Variable) : Int{
        var aNumber = a.name.substring(1).toInt()
        var bNumber = b.name.substring(1).toInt()
        if(aNumber < bNumber)
            return -1
        if(aNumber > bNumber)
            return 1
        return 0
    }

}

private class Gate(var in1: Variable, var in2: Variable, val operation: String, var output: Variable){
    fun canBeEvaluated(): Boolean{
        return (in1.v != null && in2.v != null && output.v == null)
    }

    fun evaluate(){
        require(this.canBeEvaluated())
        output.v = when (operation) {
            "AND" -> in1.v!! && in2.v!!
            "OR" -> in1.v!! || in2.v!!
            else -> in1.v!! xor in2.v!!
        }
    }

    override fun toString(): String {
        return "${in1.name} $operation ${in2.name} -> ${output.name}"
    }
}

private val gates: MutableSet<Gate> = mutableSetOf()
private val variables: MutableSet<Variable> = mutableSetOf()

fun main() {
    readGatesAndVariables("input_day24")
    propagate()
    val result = computeResult()
    println("Result: $result")
}

private fun readGatesAndVariables(inputFile: String){
    var input = helpers.readInput(inputFile)
    input.forEach {
        if(it.contains(":")){
           variables.add(createVariable(it))
        }
        if(it.contains("->")){
           gates.add(createGate(it))
        }
    }
}

private fun createVariable(line: String): Variable{
    val splitLine = line.split(": ")
    require(splitLine.count() == 2)
    val (name, value) = splitLine
    val variable = Variable(
        name,
        if (value == "1") true else false
    )
    return variable
}

private fun createGate(line: String): Gate{
    val splitLine = line.split(" ")
    listOf(splitLine[0], splitLine[2], splitLine[4]).forEach{ name ->
        if(variables.none {it.name == name}){
            variables.add(Variable(name, null))
        }
    }
    val gate = Gate(
        variables.first { it.name == splitLine[0] },
        variables.first { it.name == splitLine[2] },
        splitLine[1],
        variables.first { it.name == splitLine[4] }
    )
    return gate
}

private fun propagate(){
    while(gates.any{it.canBeEvaluated()}){
        gates.filter{it.canBeEvaluated()}.onEach{
            it.evaluate()
            updateVariable(it.output)
//            println("Evaluated the gate $it, ${it.output.name} is now ${it.output.v}.")
        }
    }
}

private fun updateVariable(variable: Variable){
    variables.removeIf { it.name == variable.name }
    variables.add(variable)
}

private fun computeResult(): Long{
    val binaryNumber = getBinaryNumber()
    var result = 0.toLong()
    binaryNumber.forEachIndexed { index, c ->
        if(c == 1)
            result += 2.power(index.toLong())
    }
    return result
}

private fun getBinaryNumber(): List<Int>{
    val zVariables = variables.filter { it.name.contains('z') }
    val comparator = VariableComparator()
    val sortedZVariables =  zVariables.toSortedSet(comparator)
    println("Sorted Z-Variables: $sortedZVariables")
    return sortedZVariables.map {
        if (it.v == true) 1 else 0
    }
}