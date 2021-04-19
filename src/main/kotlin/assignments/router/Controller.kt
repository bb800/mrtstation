package assignments.router

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import java.time.LocalDate

@Controller("/route")
class RouteController {

    @Get("/")
    @Produces(MediaType.APPLICATION_JSON)
    fun hello(): Station {
        return Station(
                code = "code",
                name = "name",
                openingDate = LocalDate.now()
        )
    }
}

data class Station(
        val code: String,
        val name: String,
        val openingDate: LocalDate,
)
