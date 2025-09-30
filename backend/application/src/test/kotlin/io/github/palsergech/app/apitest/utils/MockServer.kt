package io.github.palsergech.app.apitest.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED
import io.github.palsergech.lib.platform.time.toOffsetDateTime
import java.time.Instant
import java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME

class MockServer(
    private val wireMockServer: WireMockServer
) {

    companion object {
        private const val GET_USERS_SCENARIO = "Get updated users"
        private const val GET_USERS_SCENARIO_RETURN_USERS_STATE = "return new users"
    }
    private val objectMapper = jacksonObjectMapper()

    private data class ChangedUsersResponse(
        val changedUserIds: List<String>
    )

    private data class TestUserDto(
        val userId: String,
        val createdAt: String,
        val updatedAt: String,
        val wallets: Set<String>,
        val referrerUserId: String?
    )

    data class TestUser(val id: String, val referrerUserId: String? = null)

    fun setDefaultsMappings() {
        val defaultStubs = listOf(
            post("/keycloak/protocol/openid-connect/token")
                .willReturn(WireMock.okJson("""{"access_token": "client_access_token"}"""))
                .build(),
            request("GET", urlPathMatching("/keycloak/user-sync/users/changed-after/.*"))
                .withQueryParam("offset", equalTo("0"))
                .inScenario(GET_USERS_SCENARIO)
                .whenScenarioStateIs(STARTED)
                .willReturn(okJson(ChangedUsersResponse(changedUserIds = listOf())))
                .build()
        )
        defaultStubs.forEach(wireMockServer::addStubMapping)
    }

    suspend fun mockNewUsers(users: List<TestUser>, f: suspend () -> Unit) {
        val newUsersDto = users.map { it.toDto() }
        val stubs = listOf(
            request("GET", urlPathMatching("/keycloak/user-sync/users/changed-after/.*"))
                .inScenario(GET_USERS_SCENARIO)
                .withQueryParam("offset", equalTo("0"))
                .whenScenarioStateIs(GET_USERS_SCENARIO_RETURN_USERS_STATE)
                .willReturn(okJson(ChangedUsersResponse(newUsersDto.map { it.userId })))
                .build(),
            request("GET", urlPathMatching("/keycloak/user-sync/users/changed-after/.*"))
                .inScenario(GET_USERS_SCENARIO)
                .withQueryParam("offset", equalTo(newUsersDto.size.toString()))
                .whenScenarioStateIs(GET_USERS_SCENARIO_RETURN_USERS_STATE)
                .willReturn(okJson(ChangedUsersResponse(listOf())))
                .build(),
        ) + newUsersDto.mapIndexed { i, user ->
            get("/keycloak/user-sync/users/${user.userId}")
                .inScenario(GET_USERS_SCENARIO)
                .whenScenarioStateIs(GET_USERS_SCENARIO_RETURN_USERS_STATE)
                .willReturn(okJson(user))
                .build()
        }
        stubs.forEach(wireMockServer::addStubMapping)
        wireMockServer.setScenarioState(GET_USERS_SCENARIO, GET_USERS_SCENARIO_RETURN_USERS_STATE)
        f()
        wireMockServer.setScenarioState(GET_USERS_SCENARIO, STARTED)
        stubs.forEach(wireMockServer::removeStub)
    }


    private fun okJson(any: Any) = WireMock.okJson(objectMapper.writeValueAsString(any))

    private fun TestUser.toDto() = TestUserDto(
        userId = id,
        createdAt = Instant.now().toOffsetDateTime().format(ISO_OFFSET_DATE_TIME),
        updatedAt = Instant.now().toOffsetDateTime().format(ISO_OFFSET_DATE_TIME),
        wallets = setOf(),
        referrerUserId = referrerUserId
    )
}