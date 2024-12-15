package helpers

import kotlin.io.path.Path
import kotlin.io.path.readLines

fun Pair<Int,Int>.add(add: Pair<Int,Int>) : Pair<Int,Int>{
    return Pair(this.first + add.first, this.second + add.second)
}
fun Pair<Int,Int>.subtr(add: Pair<Int,Int>) : Pair<Int,Int>{
    return Pair(this.first - add.first, this.second - add.second)
}
