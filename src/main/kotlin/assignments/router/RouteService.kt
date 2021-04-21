package assignments.router

import assignments.algorithms.dijkstra
import assignments.parser.ParseStationInfo
import javax.inject.Singleton

@Singleton
class RouteService(private val parseStationInfo: ParseStationInfo) {
    fun findRoute(from: String, to: String): List<String> {
        val mrtGraph = parseStationInfo.generateGraph()

        return dijkstra(mrtGraph, from, to)
    }
}
