package uz.finlog.costaccounting.util

import android.icu.text.SimpleDateFormat
import java.util.Date

/**
 * @return date in format HH:mm
 */
fun Long.getDateString(): String{
    val date = Date(this)
    val format = SimpleDateFormat("HH:mm")
    return format.format(date)
}

/**
 * @return date in format dd.MM.yyyy
 */
fun Long.toDate(): String{
    val date = Date(this)
    val format = SimpleDateFormat("dd.MM.yyyy")
    return format.format(date)
}