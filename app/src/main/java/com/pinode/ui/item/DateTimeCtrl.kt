package com.pinode.ui.item

import java.time.Instant


class DateTimeCtrl {
    fun getNow() : Instant {
        return Instant.now()
    }

    fun getDeadline(selectedMinutes: Long): Instant {
        val dt = getNow()
        val deadline: Instant = dt.plusSeconds(selectedMinutes * 60)
        return deadline
    }
}
