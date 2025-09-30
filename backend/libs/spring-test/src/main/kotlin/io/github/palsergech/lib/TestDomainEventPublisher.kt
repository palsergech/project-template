package io.github.palsergech.lib

import io.github.palsergech.lib.platform.domainevents.DomainEvent
import io.github.palsergech.lib.platform.domainevents.DomainEventPublisher

class TestDomainEventPublisher: DomainEventPublisher {

    class ExternalEventsTestException : RuntimeException()

    private var eventToFail: DomainEvent? = null

    val publishedEvents: MutableMap<DomainEvent, Int> = mutableMapOf()

    override suspend fun publish(event: DomainEvent) {
        if (event == eventToFail) {
            throw ExternalEventsTestException()
        } else {
            publishedEvents.merge(event, 1, Int::plus)
        }
    }

    fun reset(eventToFail: DomainEvent? = null) {
        this.eventToFail = eventToFail
        publishedEvents.clear()
    }

    inline fun <reified T: DomainEvent> publishedEvents(): List<T> {
        return publishedEvents.keys.filterIsInstance<T>()
    }
}
