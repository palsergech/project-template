package io.github.palsergech.app.apitest.utils

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import io.github.palsergech.ApplicationConfiguration
import io.github.palsergech.lib.PostgresContainer
import io.github.palsergech.lib.platform.transaction.TransactionService

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(classes = [
    ApplicationConfiguration::class,
    ApplicationTestConfiguration::class
])
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
abstract class ApplicationIntegrationTest {

    @Value("\${local.server.port}")
    private var applicationPort = 0

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {

        private const val TEST_ADMIN_API_KEY = "admin-api-key"

        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", PostgresContainer::jdbcUrl)
            registry.add("spring.datasource.username", PostgresContainer::username)
            registry.add("spring.datasource.password", PostgresContainer::password)
            registry.add("security.api-key") { TEST_ADMIN_API_KEY }
        }
    }

    val httpClient by lazy {
        HttpClient(CIO.create()) {
            expectSuccess = false
            defaultRequest {
                url { takeFrom("http://localhost:$applicationPort") }
                contentType(ContentType.Application.Json)
            }
            install(ContentNegotiation) {
                register(ContentType.Application.Json, JacksonConverter(objectMapper))
            }
        }
    }

    val testClient by lazy {
        TestClient(
            adminApiKey = TEST_ADMIN_API_KEY,
            client = httpClient,
        )
    }

    fun truncateTables() {
        jdbcTemplate.execute("select truncate_tables_and_sequences()")
    }

    fun withTestClient(f: suspend TestClient.() -> Unit) {
        runBlocking { testClient.f() }
    }

    protected val logger: Logger = LoggerFactory.getLogger("test")
}
