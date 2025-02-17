package com.unitip.mobile.shared.commons.extensions

import android.annotation.SuppressLint
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
fun Long.appendTime(hour: Int, minute: Int): Long {
    val instant = Instant.ofEpochMilli(this)
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    val newDateTime = localDateTime.withHour(hour).withMinute(minute)
    return newDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

@SuppressLint("NewApi")
fun Long.toIsoString(): String {
    val instant = Instant.ofEpochMilli(this)
    val formatter = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault())
    return formatter.format(instant)
}

@SuppressLint("NewApi")
fun String.localDateFormat(): String {
    if (this.isNotBlank()) {
        val utcDateTime = LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
            .atZone(ZoneId.of("UTC"))
        val localDateTime = utcDateTime.withZoneSameInstant(ZoneId.systemDefault())
        return localDateTime.format(DateTimeFormatter.ofPattern("EEE, dd MMM yy"))
    }

    return ""
}

@SuppressLint("NewApi")
fun String.localTimeFormat(): String {
    if (this.isNotBlank()) {
        val utcDateTime = LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
            .atZone(ZoneId.of("UTC"))
        val localDateTime = utcDateTime.withZoneSameInstant(ZoneId.systemDefault())
        return localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    return ""
}