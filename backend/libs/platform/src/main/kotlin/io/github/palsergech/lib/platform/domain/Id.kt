package io.github.palsergech.lib.platform.domain

import java.util.*

@Suppress("unused")
data class Id<T>(
    val value: UUID
) {

    override fun toString() = value.toString()
}
