package assignments.parser

import assignments.algorithms.Graph
import assignments.algorithms.generateWeight
import assignments.datetimeprovider.DateTimeProvider
import java.time.LocalDate
import javax.inject.Singleton


@Singleton
class ParseStationInfo(
    private val dateTimeProvider: DateTimeProvider,
    private val stationInformation: StationInformation,
) {

    fun generateGraph(): Graph<String> {
        val stationNodes = convertToStationNodes(stationInformation.stationList)

        val weights = processStationNodes(stationNodes)
            .map { generateWeightMap(it, 10) }
            .flatten()
            .toMap()

        return Graph(weights)
    }

    private fun generateWeightMap(stationNode: StationNode, weight: Int): List<Pair<Pair<String, String>, Int>> {
        // retrieve adjacent stations and construct outgoing edge with weight
        return stationNode.adjacent
            .map { generateWeight(stationNode.station.name, it.name, weight) }
    }

    private fun processStationNodes(stationNodes: List<StationNode>): List<StationNode> {
        return stationNodes
            // remove non operational stations
            .filter { it.inOperation }
            // group by station name to identify interchanges
            .groupBy { it.station.name }
            // find interchange nodes and merge
            .mapValues { (_, stationNodes) ->
                if (stationNodes.size > 1) convertToInterchange(stationNodes)
                else stationNodes[0]
            }
            .values
            .toList()
    }

    private fun convertToInterchange(stations: List<StationNode>): StationNode {
        return stations.reduce { acc, next ->
            acc.copy(
                station = acc.station.copy(
                    code = acc.station.code + next.station.code
                ),
                adjacent = acc.adjacent + next.adjacent,
                isInterchange = true,
            )
        }
    }

    private fun convertToStationNodes(stationList: List<Station>): List<StationNode> =
        stationList
            // group by line code to identify previous, next stations
            .groupBy { it.code[0].substring(0, 2) }
            .mapValues { (_, stations) -> generateStationNodes(stations) }
            .values
            .flatten()

    private fun generateStationNodes(stations: List<Station>): List<StationNode> =
        stations
            .mapIndexed { index, station ->
                // generate list of previous and next station
                val adjacent = listOfNotNull(
                    if (index == 0) null else stations[index - 1],
                    if (index == stations.size - 1) null else stations[index + 1]
                )

                StationNode(
                    station = station,
                    adjacent = adjacent,
                    inOperation = dateTimeProvider.today().isAfter(station.openingDate)
                )
            }

}

data class Station(
    val name: String,
    val code: List<String>,
    val openingDate: LocalDate,
)

data class StationNode(
    val station: Station,
    val adjacent: List<Station>,
    val inOperation: Boolean,
    val isInterchange: Boolean = false,
)
