package assignments.router


import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.server.exceptions.ExceptionHandler
import javax.inject.Singleton

@Singleton
class BadRequestExceptionHandler : ExceptionHandler<BadRequestException, HttpResponse<*>> {
    override fun handle(request: HttpRequest<*>, exception: BadRequestException): HttpResponse<*> =
        HttpResponse.badRequest(BadRequestExceptionResponse(exception.message))
}
class BadRequestException(override val message: String) : Exception()
data class BadRequestExceptionResponse(val message: String)


@Singleton
class ApplicationExceptionHandler : ExceptionHandler<ApplicationException, HttpResponse<*>> {
    override fun handle(request: HttpRequest<*>, exception: ApplicationException): HttpResponse<*> =
        HttpResponse.serverError(ApplicationExceptionResponse("internal server error"))

}
class ApplicationException : Exception()
data class ApplicationExceptionResponse(val message: String)
