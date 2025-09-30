package io.github.palsergech.lib.spring.rabbitmq

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import io.github.palsergech.lib.platform.domainevents.DomainEvent
import io.github.palsergech.lib.platform.domainevents.DomainEventPublisher
import io.github.palsergech.lib.spring.rabbitmq.PublisherProperties.Companion.EVENT_TYPE_HEADER
import kotlin.reflect.KClass

class RabbitMqEventPublisher(
    private val rabbitTemplate: RabbitTemplate,
    private val objectMapper: ObjectMapper,
    routing: Map<Set<KClass<out DomainEvent>>, PublisherProperties>
) : DomainEventPublisher {

    private val routing = resolvePublishersProperties(routing)

    private val logger = LoggerFactory.getLogger(javaClass)

    override suspend fun publish(event: DomainEvent) {
        logger.info("Publishing domain event $event")

        val eventRoutingProperties = checkNotNull(routing[event::class]) {
            "An error occurred while trying to publish domain event: no routing config for ${event.type} provided"
        }

        eventRoutingProperties.forEach {
            try {
                rabbitTemplate.convertAndSend(
                    it.exchangeName,
                    it.routingKey,
                    objectMapper.writeValueAsString(event)
                ) { message ->
                    message.apply { messageProperties.setHeader(EVENT_TYPE_HEADER, event.type) }
                }
            } catch (e : Throwable) {
                logger.error("An error occurred while trying to publish domain event to rabbit", e)
                throw e
            }
        }
    }

    private fun resolvePublishersProperties(
        properties: Map<Set<KClass<out DomainEvent>>, PublisherProperties>
    ): Map<KClass<out DomainEvent>, Set<PublisherProperties>> {
        val result = mutableMapOf<KClass<out DomainEvent>, Set<PublisherProperties>>()
        properties.forEach { (key, value) -> key.forEach {
            result[it] = (result[it] ?: emptySet()) + value
        }}

        return result.toMap()
    }
}
