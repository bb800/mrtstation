package assignments.datetimeprovider

import java.time.LocalDate
import javax.inject.Singleton

interface DateTimeProvider {
    fun today(): LocalDate
}

@Singleton
class DateTimeProviderImpl: DateTimeProvider {
    override fun today(): LocalDate = LocalDate.now()
}
