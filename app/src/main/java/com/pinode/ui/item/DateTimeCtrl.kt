package com.pinode.ui.item

import java.time.Instant
import java.util.Date


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
        selectedDate: Long?,
        selectedTime: Long?
    ) {
        val dt = getNow()
        val deadline: Instant = dt.
    }
}
