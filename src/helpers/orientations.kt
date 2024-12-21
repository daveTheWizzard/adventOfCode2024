package helpers

enum class Orientation(val x: Int, val y: Int){
    NORTH(-1,0),
    SOUTH(1,0),
    WEST(0,-1),
    EAST(0,1)
}