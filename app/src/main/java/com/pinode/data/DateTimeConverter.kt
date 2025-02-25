package com.pinode.data

import androidx.room.TypeConverter
import java.time.Instant
import java.time.format.DateTimeFormatter

class DateTimeConverter {
    @TypeConverter
    fun stringToInstant(value: String): Instant =
        formatter.parse(value, Instant::from)
    @TypeConverter
    fun instantToString(value: Instant): String =
        formatter.format(value)

    private val formatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")
}