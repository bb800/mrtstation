package assignments.algorithms

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class DijkstraKtTest {

    @Test
    fun `should calculate correct shortest paths`() {
        val weights = mapOf(
            Pair("A", "B") to 2,
            Pair("A", "C") to 8,
            Pair("A", "D") to 5,
            Pair("B", "C") to 1,
            Pair("C", "E") to 3,
            Pair("D", "E") to 2
        )
        val graph = Graph(weights)

        val start = "A"
        assertEquals(listOf(start, "B", "C"), dijkstra(graph, start, "C"))
        assertEquals(listOf(start, "B", "C", "E"), dijkstra(graph, start, "E"))
        assertEquals(listOf(start, "D"), dijkstra(graph, start, "D"))

    }

    @Test
    fun `should solve computerphile problem`() {
        val start = "S"

        val graph = Graph(computerphileExample)
        val expected = listOf(start, "B", "H", "G", "E")

        assertEquals(expected, dijkstra(graph, start, "E"))
    }
}

val computerphileExample = mapOf(
    generateWeight("S", "A", 7),
    generateWeight("S", "B", 2),
    generateWeight("S", "C", 3),

    generateWeight("A", "S", 7),
    generateWeight("A", "B", 3),
    generateWeight("A", "D", 4),

    generateWeight("B", "S", 2),
    generateWeight("B", "A", 3),
    generateWeight("B", "D", 4),
    generateWeight("B", "H", 1),

    generateWeight("D", "A", 4),
    generateWeight("D", "B", 4),
    generateWeight("D", "F", 5),

    generateWeight("F", "D", 5),
    generateWeight("F", "H", 3),

    generateWeight("H", "B", 1),
    generateWeight("H", "F", 3),
    generateWeight("H", "G", 2),

    generateWeight("G", "H", 2),
    generateWeight("G", "E", 2),

    generateWeight("E", "G", 2),
    generateWeight("E", "K", 5),

    generateWeight("K", "E", 5),
    generateWeight("K", "I", 4),
    generateWeight("K", "J", 4),

    generateWeight("I", "L", 4),
    generateWeight("I", "J", 6),
    generateWeight("I", "K", 4),

    generateWeight("J", "L", 4),
    generateWeight("J", "I", 6),
    generateWeight("J", "K", 4),

    generateWeight("L", "C", 2),
    generateWeight("L", "I", 4),
    generateWeight("L", "J", 4),

    generateWeight("C", "S", 3),
    generateWeight("C", "L", 2),
)
