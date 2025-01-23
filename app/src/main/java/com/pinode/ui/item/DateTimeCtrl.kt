package com.pinode.ui.item

import android.annotation.SuppressLint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTimeCtrl {
    fun GetNow() : LocalDateTime {
        return LocalDateTime.now()
    }
    
    fun GetDeadline(countdown: String) : LocalDateTime {
         val dt = LocalDateTime.now()
         val deadline = dt.plusHours(countdown)
         return deadline
    }

    fun GetTimeStr(dt: LocalDateTime, pattern: String) : String{
        return Format(dt,pattern)
    }


    @SuppressLint("NewApi")
    fun Format(date: LocalDateTime, pattern: String) : String{
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
        return date.format(formatter)
    }

    @SuppressLint("NewApi")
    fun Parse(str: String, pattern: String) : LocalDateTime{
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
        val dt: LocalDateTime = LocalDateTime.parse(str,formatter)
        return dt
    }
}
