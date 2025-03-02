package com.pinode.data

import androidx.room.TypeConverter
import java.time.Instant

class DateTimeConverter {
    @TypeConverter
    fun stringToInstant(value: String?): Instant? {
        return value?.let { Instant.parse(it) }
    }

    @TypeConverter
    fun instantToString(value: Instant?): String? {
        return value?.toString()
    }
}