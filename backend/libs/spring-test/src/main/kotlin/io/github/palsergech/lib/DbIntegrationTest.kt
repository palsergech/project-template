package io.github.palsergech.lib

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import io.github.palsergech.lib.platform.transaction.TransactionService

@DataJdbcTest
@ActiveProfiles("test")
@ContextConfiguration(classes = [PlatformBeansConfiguration::class])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
abstract class DbIntegrationTest {

    @Autowired
    private lateinit var transactionService: TransactionService

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    companion object {

        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", PostgresContainer::jdbcUrl)
            registry.add("spring.datasource.username", PostgresContainer::username)
            registry.add("spring.datasource.password", PostgresContainer::password)
        }
    }

    @BeforeEach
    fun truncateTables() {
        transactionService.inTransaction {
            jdbcTemplate.execute("select truncate_tables_and_sequences()")
        }
    }
}

