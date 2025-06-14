package com.pinode.data

import android.util.Log
import androidx.room.TypeConverter
import java.time.LocalDateTime

class DateTimeConverter {

    @TypeConverter
    fun stringToLocalDateTime(value: String?): LocalDateTime? {
        Log.d("DateTimeConverter", "Input value: '$value'")
        return when {
            value.isNullOrEmpty() -> {
                Log.d("DateTimeConverter", "Returning null for null/empty value")
                null
            }
            value.isBlank() -> {
                Log.d("DateTimeConverter", "Returning null for blank value")
                null
            }
            else -> try {
                val result = LocalDateTime.parse(value.trim())
                Log.d("DateTimeConverter", "Parsed successfully: $result")
                result
            } catch (e: Exception) {
                Log.e("DateTimeConverter", "Parse error for value '$value': ${e.message}")
                null
            }
        }
    }
    @TypeConverter
    fun localDateTimeToString(value: LocalDateTime?): String? {
        return value?.toString()
    }
}