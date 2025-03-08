package com.pinode.ui.item

import java.time.LocalDateTime


class DateTimeCtrl {
    fun getNow() : LocalDateTime {
        return LocalDateTime.now()
    }

    fun getDeadline(selectedMinutes: Long): LocalDateTime {
        val dt = getNow()
        val deadline: LocalDateTime = dt.plusSeconds(selectedMinutes * 60)
        return deadline
    }
}
