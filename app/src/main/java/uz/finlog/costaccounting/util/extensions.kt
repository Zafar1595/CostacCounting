package uz.finlog.costaccounting.util

import android.icu.text.SimpleDateFormat
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Date
import java.util.Locale

/**
 * @return date in format HH:mm
 */
fun Long.getDateString(): String {
    val date = Date(this)
    val format = SimpleDateFormat("HH:mm")
    return format.format(date)
}

/**
 * @return date in format dd.MM.yyyy
 */
fun Long.toDate(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd.MM.yyyy")
    return format.format(date)
}

/**
 * Пример 1000000 -> 1 000 000
 */
fun Double.formatAmount(): String {
    val symbols = DecimalFormatSymbols(Locale.US).apply {
        groupingSeparator = ' ' // Устанавливаем пробел как разделитель тысяч
    }
    // #,### означает разделять тысячи, .## — до двух знаков после запятой (если они есть)
    val formatter = DecimalFormat("#,###.##", symbols)
    return formatter.format(this)
}