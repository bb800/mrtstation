package assignments.parser

import assignments.datetimeprovider.DateTimeProvider
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
internal class ParseStationInfoKtTest {

    @MockK
    lateinit var dateTimeProvider: DateTimeProvider

    @MockK
    lateinit var stationRepository: StationRepository

    @InjectMockKs
    lateinit var parseStationInfo: ParseStationInfo

    @BeforeEach
    fun setup() {
        val csvData = javaClass.getResource("/StationMap.csv")?.readText() ?: ""

        every { dateTimeProvider.today() } returns LocalDate.of(2021, 4, 21)
        every { stationRepository.stationList } returns csvData
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
            .registerModule(KotlinModule())
            .registerModule(JavaTimeModule())
            .enable(SerializationFeature.INDENT_OUTPUT)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        val actual = objectMapper.writeValueAsString(resultPojo)

        assertEquals(expected, actual)
    }

}
