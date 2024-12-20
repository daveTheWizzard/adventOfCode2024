// Used in day5.kt
class ProtocolComparator(var relation: List<Pair<Int, Int>>) : Comparator<Int> {
    override fun compare(a: Int,  b: Int) : Int{
           if(Pair(a,b) in relation)
               return -1
           if(Pair(b,a) in relation)
               return 1
           return 0
    }

}