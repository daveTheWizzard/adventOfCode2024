import helpers.*

fun main(){
    val buyerSecrets = readInput("input_day22").map{ it.toLong()}
    val prefixesLeadingToBananas = buildMap {
        buyerSecrets.forEachIndexed { secretID, secret ->
            val prefixesToBananas = getPrefixesForSecret(secret)
            prefixesToBananas.forEach { prefix, numberBananas ->
                val prefixWithID = prefix.toMutableList()
                prefixWithID.add(0, secretID)
                put(prefixWithID, numberBananas)
            }
        }
    }
    val prefixedLeadingToBananasAsList = prefixesLeadingToBananas.map { (prefix, bananas) ->
        var p = prefix.toMutableList()
        p.add(bananas)
        p
    }
    val groupedPrefixes = prefixedLeadingToBananasAsList.groupBy { it.subList(1,5) }
    val prefixesToNumberOfBananas = groupedPrefixes.map { (sublist, fullList) ->
       fullList.sumOf { it.last() }
    }
    val result = prefixesToNumberOfBananas.max()
    println("Result: $result")
    //prefixesToBananas.forEach{println(it)}
}

private fun getPrefixesForSecret(secretNumber: Long): MutableMap<List<Int>, Int> {
    val sequence = getSecretNumberSequence(secretNumber)
    val result: MutableMap<List<Int>, Int> = mutableMapOf()
    for(i in 4..sequence.lastIndex){
        val changesLeadingToSecret = getChangesLeadingToSecret(sequence, i)
        if(!result.contains(changesLeadingToSecret)) { // Monkeys use the first occurrence of the secret
            result[changesLeadingToSecret] = sequence[i].mod(10)
        }
    }
    return result
}

private fun getChangesLeadingToSecret(sequence: List<Long>, secretIndex: Int): List<Int>{
    require(secretIndex >= 4)
    val lastDigits = sequence.subList(secretIndex-4, secretIndex+1).map{it.mod(10)}
    val changes = lastDigits.zipWithNext().map { it.second - it.first }
    return changes
}

private fun getSecretNumberSequence(secretNumber: Long): List<Long>{
   return buildList {
       var secret = secretNumber
       repeat(2000){
          add(secret)
          secret = getNextSecretNumber(secret)
       }
   }
}

private fun getNextSecretNumber(secretNumber: Long): Long{
    var result = (secretNumber * 64).mix(secretNumber).prune()
    result = (result/32).mix(result).prune()
    result = (result*2048).mix(result).prune()
    return result
}

private fun Long.mix(secretNumber: Long): Long{
    return this xor secretNumber
}

private fun Long.prune(): Long{
    return this.mod(16777216.toLong())
}