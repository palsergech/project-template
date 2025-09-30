package io.github.palsergech.lib

import com.github.tomakehurst.wiremock.WireMockServer

object WiremockServer {
    val wiremock = WireMockServer().apply { start() }

    fun wiremockUrl(): String {
        return wiremock.url("").trimEnd('/')
    }

    fun resetMocks() {
        wiremock.resetAll()
    }
}