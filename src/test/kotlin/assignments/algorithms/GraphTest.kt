package assignments.algorithms

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GraphTest {
    @Test
    fun `graph constructor should generate the correct graph using weights`() {
        val weights = mapOf(
            Pair("A", "B") to 2,
            Pair("B", "A") to 10,
            Pair("A", "C") to 8,
            Pair("A", "D") to 5,
            Pair("B", "C") to 1,
            Pair("C", "E") to 3,
            Pair("D", "E") to 2
        )

        val expected = Graph(
            vertices = setOf("A", "B", "C", "D", "E"),
            edges = mapOf(
                "A" to setOf("B", "C", "D"),
                "B" to setOf("A", "C"),
                "C" to setOf("E"),
                "D" to setOf("E"),
            ),
            weights = weights,
        )
        val actual = Graph(weights)

        assertEquals(expected, actual)
    }

    @Test
    fun `graph constructor should generate the correct graph from computerphile example`() {
        // Computerphile Dijkstra: https://www.youtube.com/watch?v=GazC3A4OQTE
        val expected = Graph(
            vertices = setOf("S", "A", "B", "D", "F", "H", "G", "E", "K", "L", "I", "J", "C"),
            edges = mapOf(
                "S" to setOf("A", "B", "C"),
                "A" to setOf("S", "B", "D"),
                "B" to setOf("S", "A", "D", "H"),
                "D" to setOf("A", "B", "F"),
                "F" to setOf("D", "H"),
                "H" to setOf("B", "F", "G"),
                "G" to setOf("H", "E"),
                "E" to setOf("G", "K"),
                "K" to setOf("E", "I", "J"),
                "I" to setOf("L", "J", "K"),
                "J" to setOf("L", "I", "K"),
                "L" to setOf("C", "I", "J"),
                "C" to setOf("S", "L"),
            ),
            weights = computerphileExample,
        )
        val actual = Graph(computerphileExample)

        assertEquals(expected, actual)
    }
}
