package io.github.palsergech.lib.platform.domainevents

import org.slf4j.LoggerFactory

interface DomainEventListener {
    fun name(): String

    suspend fun handle(event: DomainEvent)

    fun unsupportedTypeError(event: DomainEvent): Nothing {
        val message = "An error occurred while trying to handle domain event: ${name()} does not support handling ${event.type} events"
        val logger = LoggerFactory.getLogger(javaClass)

        logger.error(message)
        error(message)
    }
}
