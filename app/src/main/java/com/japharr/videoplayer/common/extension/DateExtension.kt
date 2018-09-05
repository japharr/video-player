package com.japharr.videoplayer.common.extension

import java.text.DateFormat
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Japharr on 8/5/2018.
 */

/**
 * User: mcxiaoke
 * Date: 16/1/30
 * Time: 00:22
 */
object DateHelper {
    const val DF_SIMPLE_STRING = "yyyy-MM-dd HH:mm:ss"
    const val DATE_SIMPLE_STRING = "dd/MM/yyyy"
    const val DATE_SERVER_STRING = "yyyy-MM-dd"
    const val DATE_TIME_SERVER_STRING = "yyyy-MM-dd HH:mm:ss a"
    const val DATE_TIME_SIMPLE_STRING = "dd/MM/yyyy HH:mm a"
    const val DATE_TIME_HORT_STRING = "dd MMM, yyyy HH:mm a"
    const val DATER_TIME_SERVER_STRING_GEN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"

    @JvmField val DATE_TIME_SHORT_FORMAT = object : ThreadLocal<DateFormat>() {
        override fun initialValue(): DateFormat {
            return SimpleDateFormat(DATE_TIME_HORT_STRING, Locale.ENGLISH)
        }
    }
    @JvmField val DF_SIMPLE_FORMAT = object : ThreadLocal<DateFormat>() {
        override fun initialValue(): DateFormat {
            return SimpleDateFormat(DF_SIMPLE_STRING, Locale.ENGLISH)
        }
    }
    @JvmField val DATE_SIMPLE_FORMAT = object : ThreadLocal<DateFormat>() {
        override fun initialValue(): DateFormat {
            return SimpleDateFormat(DATE_SIMPLE_STRING, Locale.ENGLISH)
        }
    }
    @JvmField val DATE_TIME_SIMPLE_FORMAT = object : ThreadLocal<DateFormat>() {
        override fun initialValue(): DateFormat {
            return SimpleDateFormat(DATE_TIME_SIMPLE_STRING, Locale.ENGLISH)
        }
    }
    @JvmField val DATE_SERVER_FORMAT = object : ThreadLocal<DateFormat>() {
        override fun initialValue(): DateFormat {
            return SimpleDateFormat(DATE_SERVER_STRING, Locale.ENGLISH)
        }
    }
    @JvmField val DATE_TIME_SERVER_FORMAT = object : ThreadLocal<DateFormat>() {
        override fun initialValue(): DateFormat {
            return SimpleDateFormat(DATE_TIME_SERVER_STRING, Locale.ENGLISH)
        }
    }
    @JvmField val DATE_TIME_SERVER_GEN_FORMAT = object : ThreadLocal<DateFormat>() {
        override fun initialValue(): DateFormat {
            return SimpleDateFormat(DATER_TIME_SERVER_STRING_GEN, Locale.ENGLISH)
        }
    }
}

fun dateNow(): String = Date().asString()

fun timestamp(): Long = System.currentTimeMillis()

fun dateParse(s: String): Date = DateHelper.DF_SIMPLE_FORMAT.get().parse(s, ParsePosition(0))

fun Date.asString(format: DateFormat): String = format.format(this)

fun Date.asString(format: String): String = asString(SimpleDateFormat(format, Locale.ENGLISH))

fun Date.asString(): String = DateHelper.DF_SIMPLE_FORMAT.get().format(this)

fun Date.asSimpleDate(): String = DateHelper.DATE_SIMPLE_FORMAT.get().format(this)

fun Date.asSimpleDateTime(): String = DateHelper.DATE_TIME_SIMPLE_FORMAT.get().format(this)

fun Date.asServerDate(): String = DateHelper.DATE_SERVER_FORMAT.get().format(this)

fun Date.asServerDateTime(): String = DateHelper.DATE_TIME_SERVER_FORMAT.get().format(this)

fun Date.asGenServerDateTime(): String = DateHelper.DATE_TIME_SERVER_GEN_FORMAT.get().format(this)

fun String.asDate(): Date = DateHelper.DF_SIMPLE_FORMAT.get().parse(this, ParsePosition(0))

fun String.asDateOnly(): Date = DateHelper.DATE_SIMPLE_FORMAT.get().parse(this, ParsePosition(0))

fun Date.asShortDateTime(): String = DateHelper.DATE_TIME_SHORT_FORMAT.get().format(this)

fun Long.asDateString(): String = Date(this).asString()

fun Date.asDateOnly(): Date {
    val date = DateHelper.DATE_SIMPLE_FORMAT.get().format(this)

    return date.asDateOnly()
}