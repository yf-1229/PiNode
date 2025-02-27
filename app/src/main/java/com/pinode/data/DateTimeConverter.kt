package com.pinode.data

import androidx.room.TypeConverter
import java.time.Duration

class DateTimeConverter {
    @TypeConverter
    fun stringToDuration(value: String): Duration {
        return Duration.parse(value)
    }

    @TypeConverter
    fun durationToString(value: Duration): String {
        return value.toString()
    }
}