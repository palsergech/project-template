package io.github.palsergech.userprofile

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.KafkaListener

@Configuration
class NotificationsConfiguration {

    private val logger = LoggerFactory.getLogger(NotificationsConfiguration::class.java)

    @KafkaListener(
        id = "notifications",
        groupId = "notifications",
        topics = ["user-events"]
    )
    fun listen(data: String) {
        logger.info("""
            Received event: {}
        """.trimIndent(), data)
    }
}
