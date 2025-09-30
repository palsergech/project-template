package io.github.palsergech.lib.spring.rabbitmq

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.MessageListener
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.DirectMessageListenerContainer
import io.github.palsergech.lib.platform.domainevents.DomainEvent
import io.github.palsergech.lib.platform.domainevents.DomainEventListener
import io.github.palsergech.lib.spring.rabbitmq.PublisherProperties.Companion.EVENT_TYPE_HEADER
import java.nio.charset.Charset
import kotlin.reflect.KClass

class RabbitMqEventListener(
    private val objectMapper: ObjectMapper,
    private val eventTypes: Map<String, KClass<out DomainEvent>>,
    queue: String,
    eventListener: DomainEventListener,
    connectionFactory: ConnectionFactory
) : DirectMessageListenerContainer(connectionFactory) {

    private val listenerLogger = LoggerFactory.getLogger(RabbitMqEventListener::class.java)

    init {
        addQueueNames(queue)
        messageListener = messageListener(eventListener)
        isDefaultRequeueRejected = false

        listenerLogger.info("Started listening queue $queue for domain events")
    }

    private fun messageListener(eventListener: DomainEventListener) = MessageListener { message ->
        try {
            val eventType: String = message.messageProperties.getHeader(EVENT_TYPE_HEADER)
            val eventClass = requireNotNull(eventTypes[eventType]) {
                "Cannot find fitting class for event type $eventType"
            }

            val bodyEncoding = Charset.forName(message.messageProperties.contentEncoding)
            val messageBody = message.body.toString(bodyEncoding)

            val event = objectMapper.readValue(messageBody, eventClass.java)
            runBlocking { eventListener.handle(event) }
        } catch (e: Exception) {
            listenerLogger.error("Error while handling domain event: ${e.message}", e)
            throw e
        }
    }
}
