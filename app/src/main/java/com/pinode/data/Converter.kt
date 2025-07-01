package com.pinode.data

import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime


class Converter {

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

    @TypeConverter
    fun fromString(value: String?): MutableMap<String, Int>? {
        if (value == null) {
            return null
        }
        val mapType = object : TypeToken<MutableMap<String, Int>>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun fromMap(map: MutableMap<String, Int>?): String? {
        return Gson().toJson(map)
    }
}