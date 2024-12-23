import helpers.*

private val nodes: MutableSet<String> = mutableSetOf()
private val edges: MutableSet<Pair<String, String>> = mutableSetOf()

fun main(){
    parseInput()
    val chiefNodes = nodes.filter{
        it.startsWith("t")
    }
    val foundCliques: MutableSet<Set<String>> = mutableSetOf()
    chiefNodes.forEach {
        foundCliques.addAll(getCliques(it))
    }
    println("All cliques:")
    foundCliques.forEach { println(it) }
    println("Found ${foundCliques.count()} many cliques!")
}

private fun parseInput(){
    var input = readInput("input_day23")
    input.forEach {
        val splitLine = it.split('-')
        require(splitLine.count() == 2)
        val (node1, node2) = splitLine
        edges.add(Pair(node1, node2))
        nodes.addAll(listOf(node1, node2))
    }
}

private fun getCliques(chiefNode: String): MutableSet<Set<String>>{
    val result: MutableSet<Set<String>> = mutableSetOf()
    nodes.forEach { node1 ->
        nodes.forEach { node2 ->
            if(setOf(chiefNode, node1, node2).count() == 3){
                if(chiefNode.connectedTo(node1) && chiefNode.connectedTo(node2) && node1.connectedTo(node2)){
                    //println("Found clique consisting of $chiefNode and $node1 and $node1")
                    result.add(setOf(chiefNode, node1, node2))
                }
            }
        }
    }
    return result
}

private fun String.connectedTo(n: String): Boolean{
   return edges.contains(Pair(this, n))  || edges.contains(Pair(n, this))
}