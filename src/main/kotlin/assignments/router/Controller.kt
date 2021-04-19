package assignments.router

import assignments.parser.ParseStationInfo
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces

@Controller("/route")
class RouteController(
    private val parseStationInfo: ParseStationInfo,
) {

    @Get("/")
    @Produces(MediaType.APPLICATION_JSON)
    fun hello() = parseStationInfo.getStationInformation()

}
