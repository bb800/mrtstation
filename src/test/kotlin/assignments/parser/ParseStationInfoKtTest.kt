package assignments.parser

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class ParseStationInfoKtTest {

    @Test
    fun `extractStationInfo should parse a csv row`() {
        val testRow = "NS13,Yishun,20 December 1988"

        val expected = Station(
            code = "NS13",
            name = "Yishun",
            openingDate = LocalDate.of(1988, 12, 20)
        )
        val actual = extractStationInfo(testRow)

        assertEquals(expected, actual)
    }

    @Test
    fun `extractStationInfo should parse a csv row that contains an opening date with single digit day`() {
        val testRow = "NS12,Canberra,2 November 2019"

        val expected = Station(
            code = "NS12",
            name = "Canberra",
            openingDate = LocalDate.of(2019, 11, 2)
        )
        val actual = extractStationInfo(testRow)

        assertEquals(expected, actual)
    }

}
