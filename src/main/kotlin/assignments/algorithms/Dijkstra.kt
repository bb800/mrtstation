package assignments.algorithms

fun <T> dijkstra(graph: Graph<T>, start: T, end: T): List<T> {
    val queue = graph.vertices
        .associateWith {
            Node(
                vertex = it,
                cost = Int.MAX_VALUE,
                previous = null,
            )
        }
        .toMutableMap()

    val startNode = queue.getOrDefault(start, null)
    if (startNode != null) {
        startNode.cost = 0
    }

    val processedNodes = mutableMapOf<T, T?>()

    while(queue.isNotEmpty()) {
        val sortedQueue = sortQueue(queue)
        val cheapest = sortedQueue[0]
        val (cheapestVertex, cheapestNode) = cheapest

        queue.remove(cheapestVertex)
        processedNodes[cheapestVertex] = cheapestNode.previous

        if (cheapestVertex == end) break

        // find neighbours of cheapest
        val neighbours = graph.edges.getOrDefault(cheapestVertex, emptySet())
        neighbours.forEach { neighbour ->
            val neighbourNode = sortedQueue.find { it.first == neighbour }

            if (neighbourNode != null) {
                // retrieve neighbour cost
                val neighbourKey = graph.weights.keys
                    .first { it.first == cheapestVertex && it.second == neighbour }
                val neighbourWeight = graph.weights[neighbourKey]!!
                val altCost = cheapestNode.cost + neighbourWeight

                if (altCost < neighbourNode.second.cost) {
                    neighbourNode.second.cost = altCost
                    neighbourNode.second.previous = cheapestNode.vertex
                }
            }
        }
    }

    val processedMap = processedNodes.toMap()

    val shortestPath = traceShortestPath(processedMap, start, end)
    println("shortest path: $shortestPath")

    return shortestPath
}

private fun <T> sortQueue(queue: Map<T, Node<T>>): List<Pair<T, Node<T>>> = queue
    .toList()
    .sortedBy { (_, node) -> node.cost }

private fun <T> traceShortestPath(processedNodes: Map<T, T?>, start: T, end: T): List<T> {
    fun getShortestPath(start: T, end: T): List<T> {
        val parent = processedNodes[end]
        return if (parent == null) listOf(end)
        else getShortestPath(start, parent) + listOf(end)
    }

    return getShortestPath(start, end)
}

private class Node<T>(
    val vertex: T,
    var cost: Int,
    var previous: T?,
)
