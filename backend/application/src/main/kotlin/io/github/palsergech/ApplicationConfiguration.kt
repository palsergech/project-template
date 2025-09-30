package io.github.palsergech

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableScheduling
import io.github.palsergech.security.SecurityConfiguration
import io.github.palsergech.module1.Module1Configuration
import io.github.palsergech.lib.spring.json.DefaultJsonMapperConfiguration
import io.github.palsergech.lib.spring.scheduling.SchedulingConfiguration
import io.github.palsergech.lib.spring.transaction.DefaultBlockingTransactionService
import io.github.palsergech.rest.RestConfiguration

@Configuration
@EnableAutoConfiguration(
    exclude = [
        RabbitAutoConfiguration::class
    ]
)
@EnableScheduling
@Import(
    // API beans
    RestConfiguration::class,
    SecurityConfiguration::class,
    DefaultJsonMapperConfiguration::class,
    DefaultBlockingTransactionService::class,
    SchedulingConfiguration::class,

    // modules
    Module1Configuration::class
)
class ApplicationConfiguration