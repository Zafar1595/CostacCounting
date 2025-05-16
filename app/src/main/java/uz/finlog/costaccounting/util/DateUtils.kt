package uz.finlog.costaccounting.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.Instant
import java.util.Calendar
import java.util.Date
import java.util.Locale

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


    fun Long.toDisplayDate(): String {
        val now = System.currentTimeMillis()
        val diff = now - this

        val nowCal = Calendar.getInstance()
        val dateCal = Calendar.getInstance().apply { timeInMillis = this@toDisplayDate }

        return when {
            isSameDay(nowCal, dateCal) -> "Сегодня"
            isYesterday(nowCal, dateCal) -> "Вчера"
            else -> SimpleDateFormat("d MMMM", Locale.getDefault()).format(Date(this))
        }
    }

    private fun isSameDay(c1: Calendar, c2: Calendar): Boolean {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
    }

    private fun isYesterday(now: Calendar, target: Calendar): Boolean {
        now.add(Calendar.DAY_OF_YEAR, -1)
        return isSameDay(now, target)
    }

    fun Long.dateParse(): String{
        val date = Date(this)
        val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return format.format(date)
    }

}