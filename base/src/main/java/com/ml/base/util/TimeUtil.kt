package com.ml.base.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


object TimeUtil {

    fun long2String(timeMillis: Long, pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
        val instant = Instant.ofEpochMilli(timeMillis)
        val zone = ZoneId.systemDefault()
        val localDateTime = LocalDateTime.ofInstant(instant, zone)
        val fmt = DateTimeFormatter.ofPattern(pattern)
        val dateStr = localDateTime.format(fmt)
        return dateStr
    }

    fun timeString2Long(timeString: String, pattern: String = "yyyy-MM-dd HH:mm:ss"): Long {
        val fmt = DateTimeFormatter.ofPattern(pattern)
        val localDateTime = LocalDateTime.parse(timeString, fmt)
        val nowTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        return nowTime
    }

    fun getTodayTimeStamp(): Long {
        val todayAt0 = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0)
        val timeStamp = todayAt0.toEpochSecond(ZoneOffset.UTC)
        return timeStamp * 1000
    }
}