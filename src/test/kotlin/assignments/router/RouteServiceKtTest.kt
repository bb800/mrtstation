package assignments.router

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class RouteServiceKtTest {

    @Test
    fun `findLongestSegmentFrom should return null when start index is null`() {
        val startIndex = 0
        val testSegment = arrayOf(null, "CC1", "CC2")

        val actual = findLongestSegmentFrom(startIndex, testSegment)

        assertNull(actual)
    }

    @Test
    fun `findLongestSegmentFrom should a valid RouteSegment when start index contains a station`() {
        val startIndex = 1
        val testSegment = arrayOf(null, "CC1", "CC2", "CC3", null)

        val actual = findLongestSegmentFrom(startIndex, testSegment)
        val expected = RouteSegment(
            startIndex = 1,
            endIndex = 3,
            length = 3,
            segment = listOf("CC1", "CC2", "CC3")
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `findLongestSegmentFrom should return only the first segment`() {
        val startIndex = 1
        val testSegment = arrayOf(null, "CC1", "CC2", "CC3", null, null, "CC50", "CC51")

        val actual = findLongestSegmentFrom(startIndex, testSegment)
        val expected = RouteSegment(
            startIndex = 1,
            endIndex = 3,
            length = 3,
            segment = listOf("CC1", "CC2", "CC3")
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `findLongestSegmentFrom should handle segments of length 1`() {
        val startIndex = 1
        val testSegment = arrayOf(null, "CC1", null, null, null)

        val actual = findLongestSegmentFrom(startIndex, testSegment)
        val expected = RouteSegment(
            startIndex = 1,
            endIndex = 1,
            length = 1,
            segment = listOf("CC1")
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `findConnectingSegments return a list of connecting segments`() {
        val testSegments = mapOf(
            "CC" to arrayOf("CC1", "CC2",  "CC3",   null,   null,   null),
            "EW" to arrayOf(null,   null, "EW11", "EW12", "EW13",   null),
            "DT" to arrayOf(null,   null,   null,   null, "DT21", "DT22"),
        )

        val actual = findConnectingSegments(testSegments)
        val expected = listOf(
            listOf("CC1", "CC2", "CC3"),
            listOf("EW11", "EW12", "EW13"),
            listOf("DT21","DT22"),
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `findConnectingSegments should handle overlapping segments`() {
        val testSegments = mapOf(
            "CC" to arrayOf("CC1", "CC2",  "CC3",  "CC4",   null,   null),
            "EW" to arrayOf(null,   null, "EW11", "EW12",   null,   null),
            "DT" to arrayOf(null,   null, "DT21", "DT22", "DT23", "DT24"),
        )

        val actual = findConnectingSegments(testSegments)
        val expected = listOf(
            listOf("CC1", "CC2", "CC3", "CC4"),
            listOf("DT22", "DT23", "DT24"),
        )

        assertEquals(expected, actual)
    }


}