package assignments.parser

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Singleton


@Singleton
class ParseStationInfo {
    val stationList = initStationList()

    fun createStationMap(): Map<String, List<Node>> {
        val interchanges = stationList
            .groupBy { it.name }
            .mapValues { (_, stations) ->
                if (stations.size > 1) stations.map { it.code }
                else null
            }
            .filter { (_, codes) ->
                codes != null
            }

        println("interchanges: $interchanges")

        val allStations = stationList
            .groupBy { it.code.substring(0, 2) } // group by line code
            .mapValues { (_, stations) ->
                val nodeList = stations
                    .mapIndexed { index, station ->
                        val adjacent = listOfNotNull(
                            if (index == 0) null else stations[index - 1].code,
                            if (index == stations.size - 1) null else stations[index + 1].code
                        )

                        Node(
                            code = station.code,
                            name = station.name,
                            openingDate = station.openingDate,
                            adjacent = adjacent,
                        )
                    }
                val result = nodeList
                    .mapIndexed { index, node ->
                        node.copy(
                            previous = if (index == 0) null else nodeList[index - 1],
                            next = if (index == nodeList.size - 1) null else nodeList[index + 1]
                        )
                    }

                result
            }

        return allStations
    }

    private fun initStationList(): List<Station> {
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
    val code: String ="",
    val name: String ="",
    val openingDate: LocalDate = LocalDate.now(),
)

data class Node(
    val code: String,
    val name: String,
    val openingDate: LocalDate,
    val adjacent: List<String>,

    val previous: Node? = null,
    val next: Node? = null,
    var visited: Boolean = false,
)
