package assignments.router

import assignments.parser.ParseStationInfo
import assignments.parser.Station
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

    @Get("/")
    @Produces(MediaType.APPLICATION_JSON)
    fun getRoute(@QueryValue start: String, @QueryValue end: String) =
        routeService.findRoute(start.toLowerCase(), end.toLowerCase())

}
