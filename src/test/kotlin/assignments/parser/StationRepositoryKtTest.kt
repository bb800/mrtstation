package assignments.parser

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class StationRepositoryKtTest {

    @Test
    fun `extractStationInfo should parse a csv row properly`() {
        val testRow = "NS13,Yishun,20 December 1988"

        val expected = Station(
            name = "Yishun",
            code = listOf("NS13"),
            openingDate = listOf(LocalDate.of(1988, 12, 20),)
        )
        val actual = extractStationInfo(testRow)

        assertEquals(expected, actual)
    }

    @Test
    fun `extractStationInfo should parse a csv row with single digit day in date`() {
        val testRow = "NS12,Canberra,2 November 2022"

        val expected = Station(
            name = "Canberra",
            code = listOf("NS12"),
            openingDate = listOf(LocalDate.of(2022, 11, 2),)
        )
        val actual = extractStationInfo(testRow)

        assertEquals(expected, actual)
    }

}