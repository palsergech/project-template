package io.github.palsergech.lib

import io.github.palsergech.lib.platform.domain.Id
import io.github.palsergech.lib.platform.domain.IdFactory

object TestData {
    private const val HEX_STRING_PREFIX = "0x"
    private val HEX_DIGITS = ('0'..'9') + ('a' .. 'f')

    fun <T> IdFactory<T>.testId(i: Char): Id<T> {
        val s = i.toString()
        return idFrom("${s.repeat(8)}-${s.repeat(4)}-${s.repeat(4)}-${s.repeat(4)}-${s.repeat(12)}")
    }

    fun <T> IdFactory<T>.testId(i: Int): Id<T> {
        check(i in 1..9)
        return testId(i.toString().single())
    }
}
