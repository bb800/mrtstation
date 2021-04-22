package assignments.datetimeprovider

import java.time.LocalDate
import javax.inject.Singleton

@Singleton
class DateTimeProvider {
    fun today(): LocalDate = LocalDate.now()
}