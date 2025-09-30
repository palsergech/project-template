package io.github.palsergech.lib.platform.domain

import java.util.*

interface IdFactory<T> {
    fun idFrom(s: String) = Id<T>(UUID.fromString(s))
    fun idFrom(s: UUID) = Id<T>(s)
}

fun <T> IdFactory<T>.randomId() = Id<T>(UUID.randomUUID())
