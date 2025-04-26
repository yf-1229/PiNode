package com.pinode.ui.item

import java.time.LocalDateTime


class DateTimeCtrl {
    fun getNow() : LocalDateTime { // LocalDateTimeにする
        return LocalDateTime.now()
    }

    fun getDeadlineByMinutes(selectedMinutes: Long): LocalDateTime {
        val dt = getNow()
        val deadline: LocalDateTime = dt.plusSeconds(selectedMinutes * 60)
        return deadline
    }
}
