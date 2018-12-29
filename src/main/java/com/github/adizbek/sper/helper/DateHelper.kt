package com.github.adizbek.sper.helper

import android.text.format.DateFormat
import com.github.adizbek.sper.Sper
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {
    @JvmStatic
    var defaultDateFormat = "yyyy-MM-dd HH:mm:ss"

    fun dateFormatter(myCalendar: Calendar): String {
        return java.text.DateFormat.getDateInstance(java.text.DateFormat.LONG).format(myCalendar.time)
    }

    @JvmOverloads
    fun str2Date(date: String, pattern: String = defaultDateFormat): Date {
        val format = SimpleDateFormat(pattern, Sper.getInstance().locale)

        try {
            val d = format.parse(date)
            println(date)

            return d
        } catch (e: Exception) {
            println("Parse error ")
            return Date()
        }

    }

    fun date2MonthAndDay(date: Date): String {
        return DateFormat.format("dd-MMM", date) as String
    }

    fun date2MonthAndDay(string: String): String {
        return date2MonthAndDay(str2Date(string))
    }

    fun date2MonthAndDayAndHour(date: Date): String {
        val sdf = SimpleDateFormat("dd-MMM, HH:mm", Helper.getLocale()!!)
        return sdf.format(date)
    }

    fun dateToFullStringFormat(calendar: Calendar): String {
        return dateToFullStringFormat(calendar.time)
    }


    fun date2Custom(date: Date, pattern: String): String {
        val sdf = SimpleDateFormat(pattern, Helper.getLocale()!!)
        return sdf.format(date)
    }


    fun dateToFullStringFormat(calendar: Date): String {
        return DateFormat.format("yyyy-MM-dd HH:mm:ss", calendar.time).toString()
    }

    @JvmStatic
    fun init(dateFormat: String) {
        this.defaultDateFormat = dateFormat
    }
}

fun Long.toDateStr(pattern: String = DateHelper.defaultDateFormat): String {
    val date = Date(this)

    return DateHelper.date2Custom(date, pattern)
}

fun String.toTimestamp(pattern: String = DateHelper.defaultDateFormat): Long {
    return DateHelper.str2Date(this, pattern).time
}