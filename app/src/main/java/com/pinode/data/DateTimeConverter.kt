package com.pinode.data

import androidx.room.TypeConverter
import java.time.LocalDateTime

class DateTimeConverter {
    @TypeConverter
    fun stringToLocalDateTime(value: String?): LocalDateTime? {
        try {
            LocalDateTime.parse(value)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid date-time format: $value", e)
        }
        return value?.let { LocalDateTime.parse(it) }

    }

    @TypeConverter
    fun localDateTimeToString(value: LocalDateTime?): String? {
        return value?.toString()
    }
}