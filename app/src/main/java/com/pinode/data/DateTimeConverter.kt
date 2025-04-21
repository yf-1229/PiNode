package com.pinode.data

import androidx.room.TypeConverter
import java.time.Instant
import java.util.Date

class DateTimeConverter {
    @TypeConverter
    fun stringToInstant(value: String?): Instant? {
        try {
            Instant.parse(value)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid date-time format: $value", e)
        }
        return value?.let { Instant.parse(it) }

    }

    @TypeConverter
    fun instantToString(value: Instant?): String? {
        return value?.toString()
    }
}