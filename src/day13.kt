private class Game(val a: Pair<Int,Int>, val b: Pair<Int, Int>, val prize: Pair<Int,Int>){
    fun solve(): Int?{
        var fewestTokens = 100*3+100
        for(aPushes in 0..100){
            for(bPushes in 0..100){
                val currentTokens =  aPushes*3 + bPushes
                if(a.first*aPushes + b.first*bPushes == prize.first && a.second*aPushes + b.second*bPushes == prize.second && currentTokens <= fewestTokens){
                    fewestTokens = currentTokens
                }
            }
        }
        if (fewestTokens != 100*3 + 100)
            return fewestTokens
        else
            return null
    }

    override fun toString(): String {
        return "A: $a, B: $b, Prize: $prize "
    }
}
private var games: MutableList<Game> = mutableListOf()

fun main(){
    parseInput("input_day13")
    var result = 0
    games.forEach {
        val neededTokens = it.solve()
        result += neededTokens ?: 0
        println("It costs $neededTokens to solve the game $it")
    }
    println("Result: $result")
}

private fun parseInput(inputFileName: String){
    val input = helpers.readInput(inputFileName)
    var a = Pair(-1,-1)
    var b = Pair(-1,-1)
    var price = Pair(-1,-1)
    input.forEachIndexed{ index, line ->
        if(line == "" || index == input.lastIndex){
            games.add(Game(a!!, b!!, price!!))
        }
        when{
            line.contains("Button A") -> a = line.numbers()
            line.contains("Button B") ->  b = line.numbers()
            line.contains("Prize") -> price = line.numbers()
        }
    }
}

private fun String.numbers(): Pair<Int,Int>{
    var numbers =  this.split(Regex("\\D+"))
        .filter { it.isNotBlank() }
        .map { it.toInt()}
    require(numbers.count() == 2)
    return Pair(numbers[0], numbers[1])
}