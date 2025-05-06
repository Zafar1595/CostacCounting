package uz.finlog.costaccounting.util

import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.Instant

object DateUtils {
    fun monthBounds(month: YearMonth): Pair<Long, Long> {
        val start = month.atDay(1).atStartOfDay(ZoneId.systemDefault()).toEpochSecond()
        val end = month.atEndOfMonth().atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toEpochSecond()
        return start to end
    }

    fun todayBounds(): Pair<Long, Long> {
        val today = LocalDate.now()
        val start = today.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()
        val end = today.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toEpochSecond()
        return start to end
    }

    fun toDay(dateMillis: Long): LocalDate =
        Instant.ofEpochMilli(dateMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
}