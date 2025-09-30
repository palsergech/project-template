package io.github.palsergech.lib

import assertk.assertFailure
import assertk.assertions.hasMessage
import assertk.assertions.isInstanceOf
import io.github.palsergech.lib.platform.domain.DomainError

fun `should fail if given string is blank`(name: String, stringConsumer: (String) -> Unit) {
    assertFailure {
        stringConsumer("")
    }.isInstanceOf(DomainError::class).hasMessage("$name must not be blank")
    assertFailure {
        stringConsumer(" ")
    }.isInstanceOf(DomainError::class).hasMessage("$name must not be blank")
    assertFailure {
        stringConsumer("\n")
    }.isInstanceOf(DomainError::class).hasMessage("$name must not be blank")
    assertFailure {
        stringConsumer("\t")
    }.isInstanceOf(DomainError::class).hasMessage("$name must not be blank")
    assertFailure {
        stringConsumer("           \n\t  \n")
    }.isInstanceOf(DomainError::class).hasMessage("$name must not be blank")
}
