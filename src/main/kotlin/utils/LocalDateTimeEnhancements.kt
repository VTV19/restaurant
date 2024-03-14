package org.example.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class LocalDateTimeEnhancements {
    companion object {
        fun LocalDateTime.toBeautifulString(): String {
            return "${hour}:$minute ${dayOfMonth}.${monthNumber}.$year"
        }
        fun LocalDateTime.Companion.now(): LocalDateTime {
            return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        }
    }
}