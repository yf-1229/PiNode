package com.pinode.ui.item

import java.time.Instant
import java.time.temporal.ChronoUnit


class DateTimeCtrl {
    fun getNow() : Instant {
        return Instant.now()
    }

    fun getDeadline(selectedMinutes: Long) : Instant {
        val dt = getNow()
        val deadline = dt.plus(selectedMinutes, ChronoUnit.MINUTES)
        return deadline
    }
}
