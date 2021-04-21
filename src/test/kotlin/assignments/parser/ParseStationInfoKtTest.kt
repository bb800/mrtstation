package assignments.parser

import assignments.datetimeprovider.DateTimeProvider
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
internal class ParseStationInfoKtTest {

    @RelaxedMockK
    lateinit var dateTimeProvider: DateTimeProvider

    @MockK
    lateinit var stationInformation: StationInformation

    @InjectMockKs
    lateinit var parseStationInfo: ParseStationInfo

    @BeforeEach
    fun setup() {
        val csvData = javaClass.getResource("/StationMap.csv")?.readText() ?: ""

        every { dateTimeProvider.today() } returns LocalDate.of(2021, 4, 21)
        every { stationInformation.stationList } returns csvData
            .trim()
            .lines()
            .drop(1) // drop csv header row
            .map { extractStationInfo(it) }
    }

    @Test
    fun `generateGraph should generate a graph object`() {
        val expected = javaClass.getResource("/StationGraph.json")?.readText() ?: ""

        val resultPojo = parseStationInfo.generateGraph()

        val objectMapper = ObjectMapper()
        val actual = objectMapper.writeValueAsString(resultPojo)

        assertEquals(expected, actual)
    }

}
