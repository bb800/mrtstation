package assignments.parser

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Singleton


@Singleton
class ParseStationInfo {

    fun getStationInformation(): List<Station> {
        val csvData = javaClass.getResource("/StationMap.csv")?.readText() ?: ""

        return csvData
            .trim()
            .lines()
            .drop(1) // drop csv header row
            .map { extractStationInfo(it) }
    }

}

fun extractStationInfo(line: String): Station {
    val (code, name, openingDate) = line.split(",")

    return Station(
        code = code,
        name = name,
        openingDate = LocalDate.parse(
            openingDate,
            DateTimeFormatter.ofPattern("d MMMM yyyy")
        ),
    )
}

data class Station(
    val code: String,
    val name: String,
    val openingDate: LocalDate,
)
