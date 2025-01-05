class Lock(val heights: List<Int>){
    override fun toString(): String {
        return heights.toString()
    }
}

class Key(val heights: List<Int>){
    override fun toString(): String {
        return heights.toString()
    }
}

private val lockKeyHeight = 7
private val locks: MutableList<Lock> = mutableListOf()
private val keys: MutableList<Key> = mutableListOf()

fun main(){
   parseInput("input_day25")
    var numberOfFits = 0
    locks.forEach { lock ->
        keys.forEach { key ->
           if(fit(key, lock)) {
               numberOfFits++
           }
        }
    }
    println("Number of fits: $numberOfFits")
}

private fun parseInput(inputFileName: String){
    val input = helpers.readInput(inputFileName)
    var currentBlock = mutableListOf<String>()
    input.forEach {
       if(it != "") {
           currentBlock.add(it)
       }
       else{
           addLockOrKey(currentBlock)
           currentBlock = mutableListOf()
       }
    }
    addLockOrKey(currentBlock)
}

private fun addLockOrKey(block: MutableList<String>){
    require(block.count() == lockKeyHeight)
    var heights = MutableList(block.first().count()){0}
    block.forEach{
        it.forEachIndexed { index, c ->
            if(c == '#'){
                heights[index]++
            }
        }
    }
    if(block.isLock()){
        locks.add(Lock(heights.map { it-1 }))
    } else if(block.isKey()){
        keys.add(Key(heights.map { it-1 }))
    }
}

private fun fit(k: Key, l: Lock): Boolean{
    val addedHeights = k.heights.zip(l.heights).map{(a,b) -> a + b}
    return addedHeights.all { it <= lockKeyHeight - 2}
}

private fun List<String>.isLock(): Boolean{
    return !this[0].contains('.') && !this[lockKeyHeight-1].contains('#')
}

private fun List<String>.isKey(): Boolean{
    return !this[0].contains('#') && !this[lockKeyHeight-1].contains('.')
}