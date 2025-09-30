package io.github.palsergech.lib.platform.time

import java.time.*
import kotlin.time.toJavaDuration

fun Instant.toOffsetDateTime(): OffsetDateTime = atZone(ZoneId.systemDefault()).toOffsetDateTime()

fun Instant.toLocalDate(): LocalDate = toOffsetDateTime().toLocalDate()

fun Instant.toLocalDateTime(): LocalDateTime = toOffsetDateTime().toLocalDateTime()

fun Instant.isPast(): Boolean = this < Instant.now().plusSeconds(10)

fun Instant.isSameDayAs(other: Instant) = toLocalDate() == other.toLocalDate()

fun Instant.isOneDayBefore(other: Instant) = toLocalDate().plusDays(1) == other.toLocalDate()

fun Instant.isFarPast(other: Instant = Instant.now()) = toLocalDate() < other.toLocalDate() && !isOneDayBefore(other)

operator fun Instant.minus(kotlinDuration: kotlin.time.Duration): Instant = this - kotlinDuration.toJavaDuration()

operator fun Instant.plus(kotlinDuration: kotlin.time.Duration): Instant = this + kotlinDuration.toJavaDuration()

fun LocalDateTime.toInstant(): Instant = atZone(ZoneId.systemDefault()).toInstant()

fun LocalDateTime.toEpochMilli(): Long = toInstant().toEpochMilli()

fun LocalDateTime.toOffsetDateTime(): OffsetDateTime = toInstant().toOffsetDateTime()

fun OffsetDateTime.toSystemDefaultZoneLocalDateTime(): LocalDateTime = atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
