package helpers

import kotlin.io.path.Path
import kotlin.io.path.readLines

fun Pair<Int,Int>.add(add: Pair<Int,Int>) : Pair<Int,Int>{
    return Pair(this.first + add.first, this.second + add.second)
}
fun Pair<Int,Int>.subtr(add: Pair<Int,Int>) : Pair<Int,Int>{
    return Pair(this.first - add.first, this.second - add.second)
}

fun readInput(name: String)= Path("input/$name.txt").readLines()

fun <T> Pair<Int,Int>.onArea(area: MutableList<MutableList<T>>): Boolean{
    return (this.first in 0..area.lastIndex && this.second in 0..area.first().lastIndex)
}
