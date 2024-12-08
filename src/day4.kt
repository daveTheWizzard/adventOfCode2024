import kotlin.io.path.Path
import kotlin.io.path.readLines

const val word:String = "XMAS"

private fun readInput(name: String)= Path("input/$name.txt").readLines()

fun main(){
    val input = readInput("input_day4")
    println(part2(input))
}

fun checkX(x: Int, y: Int, input: List<String>, directions: List<Pair<Int, Int>>, targets: List<String>) : Boolean {
    val letters = directions.map{ (dx, dy) -> input[y+dy][x+dx] }
    return letters.zip(letters.drop(1)).chunked(2) {
        it[0].let { (a, b) -> "$a$b" }
    }.all{ targets.contains(it) }
}

fun part2(input: List<String>): Int {
    val lastIndex = input.first().length - 1
    val targets = listOf("MS", "SM")
    val directions = listOf(
        Pair(-1, -1),  // TopLeft
        Pair(1, 1), // BottomRight
        Pair(1, -1),  // TopRight
        Pair(-1, 1),  // BottomLeft
    )
    var matches = 0

    for (y in 1..lastIndex - 1) {
        for (x in 1..lastIndex - 1) {
            if (input[y][x] != 'A') continue
            println("$x,$y")
            if (checkX(x, y, input, directions, targets)) {
                println("MATCH")
                matches++
            }
        }
    }
    return matches
}

fun task1(){
    val input = readInput("input_day4")
    var result = 0
    val regex = "XMAS".toRegex()
    println("Diagonals")
    val (tlToBR, trToBL) = getAllDiagonals(input)
    tlToBR.forEach { result += regex.findAll(it).count() + regex.findAll(it.reversed()).count()}
    trToBL.forEach { result += regex.findAll(it).count() + regex.findAll(it.reversed()).count()}

    println()
    println("Lines")
    input.forEach { result += regex.findAll(it).count() + regex.findAll(it.reversed()).count()  }

    println()
    println("Columns")
    val transposed = transpose(input)
    transposed.forEach { result += regex.findAll(it).count() + regex.findAll(it.reversed()).count() }
    println("Result: $result")
}

fun task1_correctedSolutionToAnotherQuestion(){ //Solved for the case that there can be other letters between the XMAS letters
    val input = readInput("input_day4_test")
    var result = 0
    println("Diagonals")
    val (tlToBR, trToBL) = getAllDiagonals(input)
    tlToBR.forEach { result += numberOfMatches(input = it) + numberOfMatches(input = it.reversed())}
    trToBL.forEach { result += numberOfMatches(input = it) + numberOfMatches(input = it.reversed())}
    println()
    println("Lines")
    input.forEach { result += numberOfMatches(input = it) }
    println("Lines reversed")
    input.forEach { result += numberOfMatches(input = it.reversed())}

    println()
    println("Columns")
    val transposed = transpose(input)
    transposed.forEach { result += numberOfMatches(input = it) + numberOfMatches(input = it.reversed())}
    println("Result: $result")
}

fun visualizeMatch(matchedIndices: MutableList<Int>, input: String){
    val visualization: MutableList<Char> = input.toMutableList()
    println(visualization.mapIndexed{index, c -> if (index in matchedIndices) c else '.'})
}

fun numberOfMatches(
    nextPosToMatch: Int = 0,
    input: String,
    currentNumberOfMatches: Int = 0,
    currentIndex: Int = 0,
    matchedIndices: MutableList<Int> = mutableListOf<Int>(),
    originalInput: String = input
): Int{
    if(input == "") return 0
    var res = currentNumberOfMatches
    val shortenedInput = input.drop(1)
    if(input[0] == word[nextPosToMatch]){
        val newMatchedIndices = matchedIndices.toMutableList()
        newMatchedIndices.add(currentIndex)
        if(nextPosToMatch == word.length-1) {
            visualizeMatch(newMatchedIndices, originalInput)
            res++
        } else {
            res += numberOfMatches(
                nextPosToMatch + 1,
                shortenedInput,
                currentNumberOfMatches,
                currentIndex + 1,
                newMatchedIndices,
                originalInput
            )
        }
    }
    res += numberOfMatches(
        nextPosToMatch,
        shortenedInput,
        currentNumberOfMatches,
        currentIndex + 1,
        matchedIndices,
        originalInput
    )
    return res
}

fun transpose(matrix: List<String>): List<String> {
    if (matrix.isEmpty()) return emptyList()

    val rows = matrix.size
    val cols = matrix[0].length

    // Ensure all strings have the same length
    require(matrix.all { it.length == cols }) { "All strings must have the same length." }

    val transposed = mutableListOf<StringBuilder>()
    for (j in 0 until cols) {
        transposed.add(StringBuilder())
    }

    for (i in 0 until rows) {
        for (j in 0 until cols) {
            transposed[j].append(matrix[i][j])
        }
    }
    return transposed.map { it.toString() }
}

fun getAllDiagonals(matrix: List<String>): Pair<List<String>, List<String>> {
    val rows = matrix.size
    val cols = matrix[0].length

    val diagonalsTLtoBR = mutableListOf<String>() // Top-left to bottom-right diagonals
    val diagonalsTRtoBL = mutableListOf<String>() // Top-right to bottom-left diagonals

    // Extract top-left to bottom-right diagonals
    for (k in 0 until rows + cols - 1) {
        val diagonal = StringBuilder()
        val startRow = maxOf(0, k - cols + 1)
        val startCol = maxOf(0, cols - 1 - k)

        var i = startRow
        var j = startCol

        while (i < rows && j < cols) {
            diagonal.append(matrix[i][j])
            i++
            j++
        }
        diagonalsTLtoBR.add(diagonal.toString())
    }

    // Extract top-right to bottom-left diagonals
    for (k in 0 until rows + cols - 1) {
        val diagonal = StringBuilder()
        val startRow = maxOf(0, k - cols + 1)
        val startCol = minOf(cols - 1, k)

        var i = startRow
        var j = startCol

        while (i < rows && j >= 0) {
            diagonal.append(matrix[i][j])
            i++
            j--
        }
        diagonalsTRtoBL.add(diagonal.toString())
    }

    return Pair(diagonalsTLtoBR, diagonalsTRtoBL)
}

fun numberOfHorizontalMatches(input:String) : Int {
    val xmas = "(?=(X.*M.*A.*S))".toRegex()
    val matches =  xmas.findAll(input).map{it.groupValues[1]}
    println(matches.joinToString())
    return matches.count()
}
