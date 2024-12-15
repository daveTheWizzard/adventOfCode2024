package day15

import helpers.add

class Robot(private var x: Int, private var y: Int) {
    private fun isPushable(d: Direction, start: Pair<Int,Int>, doPush: Boolean): Boolean {
        val nextElementX = start.first + d.direction.first
        val nextElementY = start.second + d.direction.second
        val nextElement = warehouse[nextElementX][nextElementY]
        return when (nextElement) {
            '.' -> true
            '#' -> false
            else -> {
                return isBlockPushable(nextElementX, nextElementY, d, doPush)
            }
        }
    }

    private fun isBlockPushable(x: Int, y: Int, d: Direction, doPush: Boolean): Boolean {
        val leftPos: Pair<Int,Int>
        val rightPos: Pair<Int,Int>
        if (warehouse[x][y] == '[') {
            leftPos = Pair(x, y)
            rightPos = Pair(x, y + 1)
        } else {
            require(warehouse[x][y] == ']')
            rightPos = Pair(x, y)
            leftPos = Pair(x, y - 1)
        }
        val nextPosForLeft = Pair(leftPos.first + d.direction.first, leftPos.second + d.direction.second)
        val nextPosForRight = Pair(rightPos.first + d.direction.first, rightPos.second + d.direction.second)
        val isPushable = when(d){
            Direction.LEFT -> isPushable(d, leftPos, doPush)
            Direction.RIGHT -> isPushable(d, rightPos, doPush)
            else -> isPushable(d, leftPos, doPush) && isPushable(d, rightPos, doPush)
        }
        if(doPush) {
            warehouse[leftPos.first][leftPos.second] = '.'
            warehouse[rightPos.first][rightPos.second] = '.'
            warehouse[leftPos.first + d.direction.first][leftPos.second + d.direction.second] = '['
            warehouse[rightPos.first + d.direction.first][rightPos.second + d.direction.second] = ']'
        }
        return isPushable
    }

    fun move(d: Direction) {
        if (isPushable(d, Pair(this.x, this.y), false)) {
            isPushable(d, Pair(this.x, this.y), true)
            warehouse[x][y] = '.'
            this.x += d.direction.first
            this.y += d.direction.second
            warehouse[x][y] = '@'
        }
    }
}