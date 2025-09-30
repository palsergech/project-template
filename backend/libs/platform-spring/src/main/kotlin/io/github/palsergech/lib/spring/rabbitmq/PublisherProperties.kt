package io.github.palsergech.lib.spring.rabbitmq

data class PublisherProperties(
    val exchangeName: String,
    val routingKey: String = ""
) {

    companion object {
        const val EVENT_TYPE_HEADER = "Type"
    }
}
