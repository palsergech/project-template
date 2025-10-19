package io.github.palsergech.app.apitest

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import io.github.palsergech.app.apitest.utils.ApplicationIntegrationTest
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.UUID.randomUUID

class UserProfileApiTest: ApplicationIntegrationTest() {

    @Test
    fun defaultProfileIsEmpty() = runBlocking {
        val id = randomUUID()
        testClient.userApi(id.toString()).getProfile()
            .run {
                assertThat(get("id").asText()).isEqualTo(id.toString())
                assertThat(get("name")).isNull()
                assertThat(get("email")).isNull()
            }
    }

    @Test
    fun patchProfileName() = runBlocking {
        val id = randomUUID()
        val name = "test-name"
        testClient.userApi(id.toString()).patchProfileName(name)
            .run {
                assertThat(get("id").asText()).isEqualTo(id.toString())
                assertThat(get("name").asText()).isEqualTo(name)
                assertThat(get("email")).isNull()
            }
        testClient.userApi(id.toString()).getProfile()
            .run {
                assertThat(get("id").asText()).isEqualTo(id.toString())
                assertThat(get("name").asText()).isEqualTo(name)
                assertThat(get("email")).isNull()
            }
    }

    @Test
    fun patchProfileEmail() = runBlocking {
        val id = randomUUID()
        val email = "test@test.com"
        testClient.userApi(id.toString()).patchProfileEmail(email)
            .run {
                assertThat(get("id").asText()).isEqualTo(id.toString())
                assertThat(get("name")).isNull()
                assertThat(get("email").asText()).isEqualTo(email)
            }
        testClient.userApi(id.toString()).getProfile()
            .run {
                assertThat(get("id").asText()).isEqualTo(id.toString())
                assertThat(get("name")).isNull()
                assertThat(get("email").asText()).isEqualTo(email)
            }
    }


    @Test
    fun patchProfileConcurrentlyWithRetry() = runBlocking {
        val id = randomUUID()
        testClient.userApi(id.toString()).patchProfileName("first")
        for (i in 1..100) {
            val name = "test-name-$i"
            val email = "test-$i@test.com"
            logger.info("patching profile name=$name, email=$email")
            val userApi = testClient.userApi(id.toString())
            val jobs = listOf(
                async { userApi.patchProfileName(name, retry = true) },
                async { userApi.patchProfileEmail(email, retry = true) }
            )
            jobs.joinAll()
            testClient.userApi(id.toString()).getProfile()
                .run {
                    assertThat(get("id").asText()).isEqualTo(id.toString())
                    assertThat(get("name").asText()).isEqualTo(name)
                    assertThat(get("email").asText()).isEqualTo(email)
                }
        }
    }

    @Test
    fun patchProfileConcurrently() = runBlocking {
        val id = randomUUID()
        testClient.userApi(id.toString()).patchProfileName("first")
        for (i in 1..100) {
            val name = "test-name-$i"
            val email = "test-$i@test.com"
            logger.info("patching profile name=$name, email=$email")
            val userApi = testClient.userApi(id.toString())
            val jobs = listOf(
                async { userApi.patchProfileName(name) },
                async { userApi.patchProfileEmail(email) }
            )
            jobs.joinAll()
            testClient.userApi(id.toString()).getProfile()
                .run {
                    assertThat(get("id").asText()).isEqualTo(id.toString())
                    assertThat(get("name").asText()).isEqualTo(name)
                    assertThat(get("email").asText()).isEqualTo(email)
                }
        }
    }

}