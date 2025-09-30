package io.github.palsergech.lib.platform.domainevents

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

interface DomainEvent {
    val type: String
    val timestampMs: Long
}

fun DomainEvent.date(): LocalDate = LocalDate.from(
    Instant
        .ofEpochMilli(timestampMs)
        .atZone(ZoneId.systemDefault())
)
