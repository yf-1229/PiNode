package com.pinode.ui.item

import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date
import java.time.Duration


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
        selectedDate: Long,
        selectedTime: Long
    ) {
        val dt = getNow()
        val durationDate = Duration.between(dt, selectedDate)
        val deadline: Instant = dt
            .plus(selectedDate, ChronoUnit.DAYS)
            .plusSeconds(selectedTime)
    }
}
