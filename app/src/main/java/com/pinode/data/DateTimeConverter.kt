package com.pinode.data

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTimeConverter {
    @TypeConverter
    fun stringToLocalDateTime(value: String): LocalDateTime =
        try {
            LocalDateTime.parse(value, formatter)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid date-time format: $value", e)
        }

    @TypeConverter
    fun localDateTimeToString(value: LocalDateTime): String =
        formatter.format(value)

    private companion object {
        private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    }
}
