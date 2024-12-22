fun main(){
    val buyerSecrets = helpers.readInput("input_day22").map{ it.toLong()}
    var result: Long = 0
    buyerSecrets.forEach {
        var secret = it
        repeat(2000) {
            secret = getNextSecretNumber(secret)
        }
        println("Result for secret $it is $secret")
        result += secret
    }
    println("Result: $result")
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