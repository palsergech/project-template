package io.github.palsergech.app.apitest

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.palsergech.app.apitest.utils.ApplicationIntegrationTest
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class SystemEndpointsTest: ApplicationIntegrationTest() {

    @Test
    fun health() = runBlocking {
        val res = httpClient.get("api/system/health")
        assertThat(res.status).isEqualTo(HttpStatusCode.OK)
    }

    @Test
    @Disabled
    fun metrics() = runBlocking {
        val res = httpClient.get("api/system/prometheus")
        assertThat(res.status).isEqualTo(HttpStatusCode.OK)
    }

    @Test
    @Disabled
    fun openapi() = runBlocking {
        val res = httpClient.get("openapi.yaml")
        assertThat(res.status).isEqualTo(HttpStatusCode.OK)
    }

    @Test
    fun swaggerUi() = runBlocking {
        val res = httpClient.get("api/system/swagger-ui")
        assertThat(res.status).isEqualTo(HttpStatusCode.OK)
    }

    @Test
    fun appInfo() = runBlocking {
        val res = httpClient.get("api/system/info")
        assertThat(res.status).isEqualTo(HttpStatusCode.OK)
    }
}