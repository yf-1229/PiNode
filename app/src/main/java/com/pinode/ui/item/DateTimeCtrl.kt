package com.pinode.ui.item

import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date
import java.time.Duration
import java.time.LocalDateTime


class DateTimeCtrl {
    fun getNow() : Instant {
        return Instant.now()
    }

    fun getDeadlineByMinutes(selectedMinutes: Long): Instant {
        val dt = getNow()
        val deadline: Instant = dt.plusSeconds(selectedMinutes * 60)
        return deadline
    }

    fun getDeadlineByMinutes(
        selectedDate: LocalDateTime,
        selectedTime: LocalDateTime
    ): Instant {
        val dt = getNow()
    }
}
