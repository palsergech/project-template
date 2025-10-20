package io.github.palsergech.app.apitest.utils

import assertk.assertFailure
import assertk.assertions.isInstanceOf
import assertk.assertions.messageContains
import com.fasterxml.jackson.databind.JsonNode
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class TestClient(
    private val client: HttpClient,
    private val adminApiKey: String,
) {

    val adminApi = AdminApi()
    val userApi = ::UserApi

    inner class AdminApi {
        private val adminApiClient = client.config {
            defaultRequest {
                header("X-API-KEY", adminApiKey)
            }
        }
    }

    inner class UserApi(
        private val userId: String
    ) {

        private val userApiClient = client.config {
            defaultRequest {
                bearerAuth(userId)
            }
        }

        suspend fun getProfile(): JsonNode {
            return userApiClient.expectOk("get profile(userId=$userId)") {
                get("/api/profile")
            }
        }

        suspend fun patchProfileName(name: String, retry: Boolean = false): JsonNode {
            return userApiClient.expectOk("patch profile name(userId=$userId)") {
                patch("/api/profile") {
                    parameter("retry", retry)
                    useAdminApiKey()
                    contentType(ContentType.Application.Json)
                    setBody(mapOf("name" to name))
                }
            }
        }

        suspend fun patchProfileEmail(email: String, retry: Boolean = false): JsonNode {
            return userApiClient.expectOk("patch profile email(userId=$userId)") {
                patch("/api/profile") {
                    parameter("retry", retry)
                    useAdminApiKey()
                    contentType(ContentType.Application.Json)
                    setBody(mapOf("email" to email))
                }
            }
        }
    }

    internal suspend fun expectError(
        status: Int,
        errorMessage: String,
        f: suspend TestClient.() -> Unit
    ) {
        assertFailure {
            f()
        }.isInstanceOf(AssertionError::class).messageContains("""status=$status, body={"message":"$errorMessage"}""")
    }

    private suspend inline fun <reified T> HttpClient.expectOk(
        name: String,
        f: HttpClient.() -> HttpResponse
    ): T {
        val res = f()
        if (!res.status.isSuccess()) {
            throw AssertionError("failed to '$name'. status=${res.status.value}, body=${res.bodyAsText()}")
        }
        return res.body<T>()
    }

    private fun HttpRequestBuilder.useAdminApiKey() {
        header("X-API-KEY", adminApiKey)
    }
}
