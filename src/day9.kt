import java.util.LinkedList
import kotlin.io.path.Path
import kotlin.io.path.readLines

private fun readInput(name: String): LinkedList<Int>{
    val input = Path("input/$name.txt").readLines().first()
    return LinkedList(input.map { it.digitToInt() })
}
class Block(
    var length: Int,
    var id: Int
    ){
    override fun toString(): String {
        var string = ""
        if(id == -1) {
            for (i in 0..<length)
                string = "$string."
        }
        else {
            for (i in 0..<length)
                string = "$string$id "
        }
        return string
    }

    fun insert(length: Int, id: Int){
       require(this.id == -1){"Can only insert in free blocks!"}
       if(length == 0)
           return
       this.id = id
       if(this.length > length) // Given block is larger than this block
           this.length = length
    }
}

val inputD9 = readInput("input_day9")
var diskMap: MutableList<Block> = mutableListOf()

fun main(){
    readDiskMap()
    compactMapPart2()
    val result = getResultPart2()
    println("Result: $result")
}

fun readDiskMap(){
    inputD9.forEachIndexed{index, element ->
        if(index % 2 == 0)
            diskMap.add(Block(element, index/2))
        else
            diskMap.add(Block(element, -1))
    }
}
fun compactMapPart2(){
    var lastIndex = diskMap.lastIndex
    while(lastIndex >= 0){
        println(lastIndex)
        val currentBlock = diskMap[lastIndex]
        if (currentBlock.length == 0) {
            diskMap.remove(currentBlock)
        } else if(currentBlock.id != -1){
            val additionalBlockAdded = insertBlockPart2(diskMap[lastIndex])
            if(additionalBlockAdded) lastIndex++
        }
        lastIndex--
    }
}

fun insertBlockPart2(block: Block): Boolean{ // returns true if an additional block was added
    require(block.id != -1){"Can only insert non-empty blocks!"}
    var additionalBlockAdded = false
    try{
        val firstFreeBlock = diskMap.first {
            it.length >= block.length && it.id == -1 &&gi diskMap.indexOf(it) < diskMap.indexOf(block)
        }
        val remainingItems = firstFreeBlock.length - block.length
        if(remainingItems > 0) { // Block does not fill current block completely
            diskMap.add(diskMap.indexOf(firstFreeBlock) + 1, Block(remainingItems, -1))
            additionalBlockAdded = true
        }
        firstFreeBlock.insert(block.length, block.id)
        block.id = -1
    } catch(e: NoSuchElementException){
        //println("No free space found")
    }
    return additionalBlockAdded
}

fun compactMapPart1(){
    var lastIndex = diskMap.lastIndex
    while(lastIndex >= 0){
        val currentBlock = diskMap[lastIndex]
        if(diskMap[lastIndex].id != -1) {
            insertBlockPart1(diskMap[lastIndex])
        }
        if(diskMap[lastIndex].id != -1 && currentBlock.length > 0)
            return // no more remaining free spaces
        while(diskMap.last().id == -1 || diskMap.last().length <= 0) {
            diskMap.removeLast()
        }
        lastIndex = diskMap.lastIndex
    }
}
fun insertBlockPart1(block: Block){
    require(block.id != -1){"Can only insert non-empty blocks!"}
    var remainingItems = block.length
    diskMap.forEachIndexed { index, currentBlock ->
       if(currentBlock.id == -1) {  // Is a free block
           remainingItems -= currentBlock.length
           if(block.length < currentBlock.length) { // Block does not fill current block completely
               diskMap.add(index + 1, Block(currentBlock.length - block.length, -1))
               currentBlock.length = block.length
           }
           currentBlock.insert(block.length, block.id)
           block.length = remainingItems
           if(remainingItems <= 0)
               return
       }
    }
}

fun mergeFreeBlocks(){
    val newDiskMap = mutableListOf<Block>()
    diskMap.forEach {
        if (it.id == -1 && newDiskMap.isNotEmpty() && newDiskMap.last().id == -1) {
            newDiskMap.last().length += it.length
        } else {
            newDiskMap.add(it)
        }
    }
    diskMap = newDiskMap
}

fun getResultPart2(): ULong {
    mergeFreeBlocks()
    var res: ULong = 0u
    var index = 0
    diskMap.forEach {
        for(j in 0..<it.length){
            if(it.length > 0 && it.id != -1) res += (index * it.id).toUInt()
                index++
            }
        }
    return res
}

fun getResult(): ULong {
    var res: ULong = 0u
    var index = 0
    diskMap.forEach {
        if(it.length > 0){
            for(j in 0..<it.length){
               res += (index * it.id).toUInt()
               index++
            }
        }
    }
    return res
}

