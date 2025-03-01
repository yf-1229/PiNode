package com.pinode.ui.item

import java.time.Instant

class DateTimeCtrl {
    fun GetNow() : Instant {
        return Instant.now()
    }

    fun GetDeadline(selectedMinutes: Long) : Instant {
        val dt = GetNow()
        val deadline = dt.plusMillis(selectedMinutes)
        return deadline
    }
}
