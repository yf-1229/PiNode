package com.pinode.data

import androidx.room.TypeConverter
import java.time.Instant

class DateTimeConverter {
    @TypeConverter
    fun stringToInstant(value: String): Instant {
        return Instant.parse(value)
    }

    @TypeConverter
    fun instantToString(value: Instant): String {
        return value.toString()
    }
}