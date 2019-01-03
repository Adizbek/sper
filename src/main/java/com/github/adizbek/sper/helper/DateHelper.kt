package com.github.adizbek.sper.helper

import android.text.format.DateFormat
import com.github.adizbek.sper.Sper
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

object DateHelper {
    @JvmStatic
    val fullDateFormat = "yyyy-MM-dd HH:mm:ss"

    @JvmStatic
    var defaultDateFormat = fullDateFormat

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

    fun convertStrDate(date: String, toPattern: String, fromPattern: String = defaultDateFormat): String {
        return date2Custom(str2Date(date, fromPattern), toPattern)
    }

    fun date2MonthAndDay(date: Date): String {
        return DateFormat.format("dd-MMM", date) as String
    }

    fun date2MonthAndDay(string: String): String {
        return date2MonthAndDay(str2Date(string))
    }

    fun date2MonthAndDayAndHour(date: Date): String {
        val sdf = SimpleDateFormat("dd-MMM, HH:mm", Sper.getInstance().locale)
        return sdf.format(date)
    }

    fun dateToFullStringFormat(calendar: Calendar): String {
        return dateToFullStringFormat(calendar.time)
    }


    fun date2Custom(date: Date, pattern: String): String {
        val sdf = SimpleDateFormat(pattern, Sper.getInstance().locale)
        return sdf.format(date)
    }


    fun dateToFullStringFormat(calendar: Date): String {
        return DateFormat.format(fullDateFormat, calendar.time).toString()
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
    return try {
        DateHelper.str2Date(this, pattern).time
    } catch (e: java.lang.Exception) {
        0L
    }
}

fun String.conv2Date(toPattern: String, fromPattern: String = DateHelper.defaultDateFormat): String {
    return DateHelper.convertStrDate(this, toPattern, fromPattern)
}

fun String.dateFull(fromPattern: String = DateHelper.defaultDateFormat): String {
    return this.conv2Date(DateHelper.fullDateFormat, fromPattern)
}

fun Calendar.setMonthStart() {
    this.set(this.get(Calendar.YEAR), this.get(Calendar.MONTH), 1, 0, 0, 0)
    this.set(Calendar.MILLISECOND, 0)
}

fun Calendar.setMonthStartC(): Calendar {
    return (this.clone() as Calendar).apply {
        setMonthStart()
    }
}

fun Calendar.setMonthEnd() {
    this.set(this.get(Calendar.YEAR), this.get(Calendar.MONTH), 1, 0, 0, 0)
    this.set(Calendar.MILLISECOND, 0)
    this.add(Calendar.MONTH, 1)
    this.add(Calendar.SECOND, -1)
}

fun Calendar.setMonthEndC(): Calendar {
    return (this.clone() as Calendar).apply {
        setMonthEnd()
    }
}