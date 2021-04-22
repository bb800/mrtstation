package assignments.parser

import assignments.algorithms.Graph
import assignments.algorithms.generateWeight
import assignments.datetimeprovider.DateTimeProvider
import javax.inject.Singleton


@Singleton
class ParseStationInfo(
    private val dateTimeProvider: DateTimeProvider,
    private val stationRepository: StationRepository,
) {

    fun generateGraph(): MrtData {
        val stationNodes = getStationNodes(stationRepository.stationList)
            .let { updateWithInterchangeNodes(it) }

        val stationMap = stationNodes.associateBy { it.station.name }

        val weights = stationNodes
            .map { generateStationWeights(it, 10) }
            .flatten()
            .toMap()

        return MrtData(
            stationMap = stationMap,
            graph = Graph(weights),
        )
    }

    private fun getStationNodes(stations: List<Station>): List<StationNode> =
        stations
            // group by line code to identify previous, next stations
            .groupBy { it.code[0].substring(0, 2) }
            .mapValues { (_, stations) -> generateStationNodes(stations) }
            .values
            .flatten()

    private fun generateStationWeights(stationNode: StationNode, weight: Int): List<Pair<Pair<String, String>, Int>> {
        // retrieve adjacent stations and construct outgoing edge with weight
        return stationNode.adjacent
            .map { generateWeight(stationNode.station.name, it.name, weight) }
    }

    private fun updateWithInterchangeNodes(stationNodes: List<StationNode>): List<StationNode> {
        return stationNodes
            // remove non operational stations. before, processing, there is only 1 opening date
            .filter { dateTimeProvider.today().isAfter(it.station.openingDate[0]) }
            // group by station name to identify interchanges
            .groupBy { it.station.name }
            // find interchange nodes and merge
            .mapValues { (_, stationNodes) ->
                if (stationNodes.size > 1) combineStationNodes(stationNodes)
                else stationNodes[0]
            }
            .values
            .toList()
    }

    private fun combineStationNodes(stations: List<StationNode>): StationNode {
        return stations.reduce { acc, next ->
            acc.copy(
                station = acc.station.copy(
                    code = acc.station.code + next.station.code,
                    openingDate = acc.station.openingDate + next.station.openingDate
                ),
                adjacent = acc.adjacent + next.adjacent,
                isInterchange = true,
            )
        }
    }

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
                )
            }

}

data class MrtData(
    val stationMap: Map<String, StationNode>,
    val graph: Graph<String>,
)

data class StationNode(
    val station: Station,
    val adjacent: List<Station>,
    val isInterchange: Boolean = false,
)
