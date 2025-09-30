package io.github.palsergech.lib

import org.flywaydb.core.Flyway
import org.testcontainers.containers.PostgreSQLContainer

object PostgresContainer {

    private val container = PostgreSQLContainer("postgres:latest")
        .withDatabaseName("achievements")
        .withUsername("postgres")
        .withPassword("postgres")
        .withReuse(true)

    fun jdbcUrl() = container.jdbcUrl
    fun username() = container.username
    fun password() = container.password

    init {
        container.start()

        Flyway.configure()
            .dataSource(container.jdbcUrl, container.username, container.password)
            .load()
            .migrate()
    }
}