package io.github.palsergech.lib.platform.domain

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

object DomainChecks {

    @OptIn(ExperimentalContracts::class)
    inline fun requireNotBlank(value: String?, msg: () -> String) {
        contract {
            returns() implies (value != null)
        }
        require(!value.isNullOrBlank(), msg)
    }

    fun requireNull(value: Any?, msg: () -> String) {
        require(value == null, msg)
    }

    @OptIn(ExperimentalContracts::class)
    inline fun require(value: Boolean, msg: () -> String) {
        contract {
            returns() implies value
        }
        if (!value) throw DomainError(msg())
    }

    @OptIn(ExperimentalContracts::class)
    inline fun <T: Any?> requireNotNull(value: T, msg: () -> String) {
        contract {
            returns() implies (value != null)
        }
        if (value == null) throw DomainError(msg())
    }
}