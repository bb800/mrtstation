package assignments.router

import assignments.parser.ParseStationInfo
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.http.annotation.QueryValue

@Controller("/route")
class RouteController(
    private val parseStationInfo: ParseStationInfo,
    private val routeService: RouteService,
) {

    // TODO: Remove
    @Get("/test")
    @Produces(MediaType.APPLICATION_JSON)
    fun test() = parseStationInfo.generateGraph()

    @Get("/")
    @Produces(MediaType.APPLICATION_JSON)
    fun getRoute(@QueryValue from: String, @QueryValue to: String): List<String> =
        routeService.findRoute(from, to)

}
