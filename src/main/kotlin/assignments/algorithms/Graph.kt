package assignments.algorithms

fun <T> generateWeight(a: T, b:T, weight: Int): Pair<Pair<T, T>, Int> {
    return Pair(a, b) to weight
}

data class Graph<T>(
    val vertices: Set<T>,
    val edges: Map<T, Set<T>>,
    val weights: Map<Pair<T, T>, Int>
) {
    constructor(weights: Map<Pair<T, T>, Int>) : this(
        vertices = weights.keys.toList().getVertices(),
        edges = weights.keys
            .groupBy { it.first }
            .mapValues(::generateEdge)
            .withDefault { emptySet() },
        weights = weights
    )
}

private fun <T> List<Pair<T, T>>.getVertices(): Set<T> = this
    .map { (a, b) -> listOf(a, b) }
    .flatten()
    .toSet()

private fun <T> generateEdge(node: Map.Entry<T, List<Pair<T, T>>>): Set<T> {
    val (edge, otherEdges) = node

    return otherEdges
        .map { (a, b) -> listOf(a, b) }
        .flatten()
        .filter { it != edge }
        .toSet()
}
