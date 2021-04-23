package assignments.router

import assignments.datetimeprovider.DateTimeProvider
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.extension.ExtendWith
import java.net.URLEncoder
import java.time.LocalDate
import javax.inject.Inject


@MicronautTest
@ExtendWith(MockKExtension::class)
internal class RouteControllerTest {

    @MockBean(DateTimeProvider::class)
    fun dateTimeProvider(): DateTimeProvider = mockk()

    @Inject
    @field:Client(value = "/")
    lateinit var rxClient: RxHttpClient

    @Inject
    lateinit var dateTimeProvider: DateTimeProvider

    @BeforeEach
    fun setup() {
        every { dateTimeProvider.today() } returns LocalDate.of(2021, 4, 21)
    }

    private val jsonMapper = jacksonObjectMapper()
        .registerModule(KotlinModule())

    @Test
    fun `route should return instructions on how to get from a start station to an end station`() {
        val startParam = URLEncoder.encode("\"changi airport\"", "utf-8")
        val endParam = URLEncoder.encode("\"ang mo kio\"", "utf-8")
        val request: HttpRequest<*> = HttpRequest.GET<Any>("/route?start=$startParam&end=$endParam")

        val actual = rxClient.toBlocking().retrieve(request)

        val actualPojo: List<String> = jsonMapper.readValue(actual)
        val expectedPojo = listOf(
            "Board at Changi Airport (CG2)",
            "Alight at Tanah Merah (CG0)",
            "Change to the EW line by boarding at Tanah Merah (EW4)",
            "Alight at Paya Lebar (EW8)",
            "Change to the CC line by boarding at Paya Lebar (CC9)",
            "Alight at Bishan (CC15)",
            "Change to the NS line by boarding at Bishan (NS17)",
            "Alight at Ang Mo Kio (NS16)"
        )

        assertEquals(expectedPojo, actualPojo)

    }

    @Test
    fun `route should handle parameters in any case`() {
        val startParam = URLEncoder.encode("\"CHANGI AIRPORT\"", "utf-8")
        val endParam = URLEncoder.encode("\"ang mo kio\"", "utf-8")
        val request: HttpRequest<*> = HttpRequest.GET<Any>("/route?start=$startParam&end=$endParam")

        val actual = rxClient.toBlocking().retrieve(request)

        val actualPojo: List<String> = jsonMapper.readValue(actual)
        val expectedPojo = listOf(
            "Board at Changi Airport (CG2)",
            "Alight at Tanah Merah (CG0)",
            "Change to the EW line by boarding at Tanah Merah (EW4)",
            "Alight at Paya Lebar (EW8)",
            "Change to the CC line by boarding at Paya Lebar (CC9)",
            "Alight at Bishan (CC15)",
            "Change to the NS line by boarding at Bishan (NS17)",
            "Alight at Ang Mo Kio (NS16)"
        )

        assertEquals(expectedPojo, actualPojo)

    }

    @Test
    fun `route should handle parameters without quotes`() {
        val startParam = URLEncoder.encode("bedok north", "utf-8")
        val endParam = URLEncoder.encode("kaki bukit", "utf-8")
        val request: HttpRequest<*> = HttpRequest.GET<Any>("/route?start=$startParam&end=$endParam")

        val actual = rxClient.toBlocking().retrieve(request)

        val actualPojo: List<String> = jsonMapper.readValue(actual)
        val expectedPojo = listOf(
            "Board at Bedok North (DT29)",
            "Alight at Kaki Bukit (DT28)",
        )
        assertEquals(expectedPojo, actualPojo)
    }

    @Test
    fun `route should handle single station journey`() {
        val startParam = URLEncoder.encode("\"bedok north\"", "utf-8")
        val endParam = URLEncoder.encode("\"kaki bukit\"", "utf-8")
        val request: HttpRequest<*> = HttpRequest.GET<Any>("/route?start=$startParam&end=$endParam")

        val actual = rxClient.toBlocking().retrieve(request)

        val actualPojo: List<String> = jsonMapper.readValue(actual)
        val expectedPojo = listOf(
            "Board at Bedok North (DT29)",
            "Alight at Kaki Bukit (DT28)",
        )
        assertEquals(expectedPojo, actualPojo)
    }

    @Test
    fun `route should handle same station as start and end`() {
        val param = URLEncoder.encode("\"changi airport\"", "utf-8")
        val request: HttpRequest<*> = HttpRequest.GET<Any>("/route?start=$param&end=$param")

        val actual = rxClient.toBlocking().retrieve(request)

        val actualPojo: List<String> = jsonMapper.readValue(actual)
        val expectedPojo = listOf("You are already here!")

        assertEquals(expectedPojo, actualPojo)
    }

    @Test
    fun `route should single segment journeys with no interchanges`() {
        val startParam = URLEncoder.encode("\"bedok\"", "utf-8")
        val endParam = URLEncoder.encode("\"eunos\"", "utf-8")
        val request: HttpRequest<*> = HttpRequest.GET<Any>("/route?start=$startParam&end=$endParam")

        val actual = rxClient.toBlocking().retrieve(request)

        val actualPojo: List<String> = jsonMapper.readValue(actual)
        val expectedPojo = listOf(
            "Board at Bedok (EW5)",
            "Alight at Eunos (EW7)",
        )

        assertEquals(expectedPojo, actualPojo)
    }

    @Test
    fun `route should return a bad request when start or end station is missing`() {
        val param = URLEncoder.encode("\"bedok\"", "utf-8")

        val ex1 = assertThrows(HttpClientResponseException::class.java) {
            rxClient.toBlocking().exchange<Any>("/route")
        }
        assertEquals(400, ex1.status.code)

        val ex2 = assertThrows(HttpClientResponseException::class.java) {
            rxClient.toBlocking().exchange<Any>("/route?start=$param")
        }
        assertEquals(400, ex2.status.code)

        val ex3 = assertThrows(HttpClientResponseException::class.java) {
            rxClient.toBlocking().exchange<Any>("/route?end=$param")
        }
        assertEquals(400, ex3.status.code)
    }


}