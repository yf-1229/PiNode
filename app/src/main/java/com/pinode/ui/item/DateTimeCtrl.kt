package com.pinode.ui.item

import android.annotation.SuppressLint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTimeCtrl {
    fun GetNow() : LocalDateTime {
        return LocalDateTime.now()
    }

    fun GetNowStr(pattern: String) : String{
        val dt = GetNow()
        return Format(dt,pattern)
    }

    fun GetDeadline(countdown: String) {

    }


    @SuppressLint("NewApi")
    fun Format(date : LocalDateTime, pattern: String) : String{
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
        return date.format(formatter)
    }

    @SuppressLint("NewApi")
    fun Parse(str : String, pattern: String) : LocalDateTime{
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
        val dt: LocalDateTime = LocalDateTime.parse(str,formatter)
        return dt
    }
}
