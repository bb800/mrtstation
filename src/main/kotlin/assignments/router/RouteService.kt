package assignments.router

import assignments.algorithms.dijkstra
import assignments.parser.ParseStationInfo
import assignments.parser.Station
import assignments.parser.StationNode
import javax.inject.Singleton

@Singleton
class RouteService(
    private val parseStationInfo: ParseStationInfo,
) {

    // TODO: Add logging

    fun findRoute(start: String, end: String): List<String> {
        val (stationMap, mrtGraph) = parseStationInfo.generateGraph()

        if (stationMap[start] == null) throw BadRequestException("starting station could not be found")
        if (stationMap[end] == null) throw BadRequestException("ending station could not be found")

        val route = dijkstra(mrtGraph, start, end)
        return generateInstructions2(stationMap, route)
    }

    private fun generateInstructions2(stationMap: Map<String, StationNode>, route: List<String>): List<String> {
        val stations = route
            .map { stationMap[it]?.station ?: throw ApplicationException() }

        println("stations: $stations")
        val segments = generateSegmentMap(stations)
        println("segments: $segments")

        val journey = findConnectingSegments(segments)
        println("journey: $journey")

        val stationCodeMap = stationMap
            .map { (stationName, stationNode) ->
                // Capitalize station name
                val displayName = stationName
                    .split(" ")
                    .joinToString(" ") { it.capitalize() }

                stationNode.station.code.map {
                    it to displayName
                }

            }
            .flatten()
            .toMap()

        println("stationNameMap: $stationCodeMap")
        val instructions = generateJourneyInstructions(stationCodeMap, journey)
        println("instructions: $instructions")

        return instructions
    }

    private fun generateJourneyInstructions(stations: Map<String, String>, journey: List<List<String>>): List<String> {
        val first = journey.first()
        val rest = journey.drop(1)

        val initialInstructions = generateBeginningInstructions(stations, first)
        val lineChangeInstructions = rest
            .map { generateLineChangeInstructions(stations, it) }
            .flatten()

        return initialInstructions + lineChangeInstructions
    }

    private fun generateBeginningInstructions(stations: Map<String, String>, stationCodes: List<String>): List<String> {
        val firstStationCode = stationCodes.first()
        val firstStationName = stations[firstStationCode]
        val lastStationCode = stationCodes.last()
        val lastStationName = stations[lastStationCode]

        return listOf(
            "Board at $firstStationName ($firstStationCode)",
            "Alight at $lastStationName ($lastStationCode)",
        )
    }

    private fun generateLineChangeInstructions(
        stations: Map<String, String>,
        stationCodes: List<String>
    ): List<String> {
        val firstStationCode = stationCodes.first()
        val firstStationName = stations[firstStationCode]
        val lastStationCode = stationCodes.last()
        val lastStationName = stations[lastStationCode]
        val lineCode = firstStationCode.substring(0, 2)

        return listOf(
            "Change to the $lineCode line by boarding at $firstStationName ($firstStationCode)",
            "Alight at $lastStationName ($lastStationCode)",
        )
    }
}

fun generateSegmentMap(
    stations: List<Station>,
): Map<String, Array<String?>> {
    val stationCodesList = stations
        .map { it.code }

    val uniqueLines = stationCodesList
        .flatten()
        .map { it.substring(0, 2) }
        .toSet()
        .toList()

    // initialize segments, map of line code to Array<String>,
    // and use it to store station codes in position
    // eg.
    // {
    //      "CC": ["CC1", "CC2",  null,  null,  null],
    //      "EW": [ null, "EW1", "EW2", "EW3",  null],
    //      "DT": [ null,  null,  null, "DT1", "DT2"],
    // }
    val segments = uniqueLines
        .map<String, Pair<String, Array<String?>>> { it to Array(stations.size) { null } }
        .toMap()

    // iterate over station codes to fill up the segments map
    stationCodesList.forEachIndexed { index, codeList ->
        codeList.forEach { code ->
            val lineCode = code.substring(0, 2)
            segments[lineCode]?.set(index, code)
        }
    }
    return segments
}

fun findConnectingSegments(segments: Map<String, Array<String?>>): List<List<String>> {
    var index = 0
    val segmentSize = segments.values.first().size
    val journey = mutableListOf<List<String>>()

    while (index < segmentSize - 1) {
        val longestRouteSegment = segments
            .values
            .map { findLongestSegmentFrom(index, it) }
            .maxWithOrNull(Comparator.comparingInt { it?.length ?: 0 })

        if (longestRouteSegment != null) {
            journey.add(longestRouteSegment.segment)
            index = longestRouteSegment.endIndex
        }
    }

    return journey.toList()
}

fun findLongestSegmentFrom(startIndex: Int, input: Array<String?>): RouteSegment? {
    if (input[startIndex] == null) return null
    else {
        var endIndex = startIndex
        val segment = mutableListOf<String>()
        for (i in startIndex until input.size) {
            val currentStation = input[i]
            if (currentStation != null) {
                segment.add(currentStation)
                endIndex++
            } else break
        }

        return RouteSegment(
            startIndex = startIndex,
            endIndex = endIndex - 1,
            segment = segment.toList(),
            length = segment.size,
        )
    }
}

data class RouteSegment(
    val startIndex: Int,
    val endIndex: Int,
    val segment: List<String>,
    val length: Int,
)
