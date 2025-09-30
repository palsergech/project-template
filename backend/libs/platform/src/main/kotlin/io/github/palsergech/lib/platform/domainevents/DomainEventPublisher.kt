package io.github.palsergech.lib.platform.domainevents

interface DomainEventPublisher {
    suspend fun publish(event: DomainEvent)
}

