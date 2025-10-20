package io.github.palsergech

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.annotation.EnableRetry
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@SpringBootApplication(
    exclude = [
        RabbitAutoConfiguration::class
    ]
)
@EnableScheduling
@EnableTransactionManagement
@EnableRetry
@Import(
    ApplicationConfiguration.SerializationConfig::class,
    ApplicationConfiguration.OpenApiResourceLoader::class
)
class ApplicationConfiguration {

    @Configuration
    class SerializationConfig {

        @Bean
        internal fun objectMapper() = jacksonObjectMapper().apply {
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            registerModule(JavaTimeModule())
        }
    }

    @Configuration
    class OpenApiResourceLoader : WebMvcConfigurer {
        override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
            registry.addResourceHandler("/openapi.yaml")
                .addResourceLocations("classpath:/openapi.yaml")
        }
    }

}