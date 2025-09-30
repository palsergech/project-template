package io.github.palsergech.lib.spring.persistence

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import io.github.palsergech.lib.spring.persistence.jdbc.latestsync.JdbcLatestSyncPersister

@Configuration
@Import(
    JdbcLatestSyncPersister::class
)
@EnableJdbcRepositories("io.github.palsergech.lib.spring.persistence.jdbc")
class PersistenceConfiguration
