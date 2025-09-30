package io.github.palsergech.lib.spring.domainevents

import org.slf4j.LoggerFactory
import io.github.palsergech.lib.platform.domainevents.DomainEvent
import io.github.palsergech.lib.platform.domainevents.DomainEventListener
import io.github.palsergech.lib.platform.domainevents.DomainEventPublisher
import io.github.palsergech.lib.platform.transaction.TransactionService

class DefaultDomainEventPublisher(
    private val transactionService: TransactionService,
    private val listeners: List<DomainEventListener>
) : DomainEventPublisher {

    private val logger = LoggerFactory.getLogger(DomainEventPublisher::class.java)

    override suspend fun publish(event: DomainEvent) {
        if (!transactionService.coTransactionExists()) {
            error("Transaction must be started before publishing events")
        }
        logger.info("Domain event published: $event")
        listeners.forEach { listener ->
            try {
                listener.handle(event)
            } catch (e: Exception) {
                logger.error("Error handling event by listener: ${listener.name()}", e)
                throw e
            }
        }
    }
}