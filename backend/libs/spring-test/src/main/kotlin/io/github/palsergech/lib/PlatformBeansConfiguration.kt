package io.github.palsergech.lib

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import
import io.github.palsergech.lib.spring.json.DefaultJsonMapperConfiguration
import io.github.palsergech.lib.spring.persistence.PersistenceConfiguration
import io.github.palsergech.lib.spring.rest.DefaultExceptionHandler
import io.github.palsergech.lib.spring.transaction.DefaultBlockingTransactionService

@TestConfiguration
@Import(
    DefaultJsonMapperConfiguration::class,
    DefaultExceptionHandler::class,
    DefaultBlockingTransactionService::class,
    PersistenceConfiguration::class,
    TestDomainEventPublisher::class
)
internal class PlatformBeansConfiguration
