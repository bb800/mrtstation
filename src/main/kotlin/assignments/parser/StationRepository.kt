package assignments.parser

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Singleton

@Singleton
class StationRepository {
    // cache list on initialization
    val stationList = initStationListFromCsv()

    private fun initStationListFromCsv(): List<Station> {
        val csvData = javaClass.getResource("/StationMap.csv")?.readText() ?: ""

        return csvData
            .trim()
            .lines()
            .drop(1) // drop csv header row
            .map { extractStationInfo(it) }
    }
}

fun extractStationInfo(line: String): Station {
    val (code, name, openingDateString) = line.split(",")

    val openingDate = LocalDate.parse(
        openingDateString,
        DateTimeFormatter.ofPattern("d MMMM yyyy")
    )

    return Station(
        name = name.toLowerCase(),
        code = listOf(code),
        openingDate = listOf(openingDate),
    )
}

data class Station(
    val name: String,
    val code: List<String>,
    val openingDate: List<LocalDate>,
)