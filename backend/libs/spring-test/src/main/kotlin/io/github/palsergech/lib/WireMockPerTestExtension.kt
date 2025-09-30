package io.github.palsergech.lib

import com.github.tomakehurst.wiremock.WireMockServer
import org.junit.jupiter.api.extension.*
import org.slf4j.LoggerFactory

class WireMockPerTestExtension: BeforeEachCallback, AfterEachCallback, ParameterResolver {
    private val logger = LoggerFactory.getLogger(WireMockPerTestExtension::class.java)

    private lateinit var wireMock: WireMockServer

    override fun supportsParameter(parameterContext: ParameterContext?, extensionContext: ExtensionContext?): Boolean {
        return parameterContext?.parameter?.type == WireMockServer::class.java
    }

    override fun resolveParameter(parameterContext: ParameterContext?, extensionContext: ExtensionContext?): Any {
        return wireMock
    }

    override fun beforeEach(context: ExtensionContext?) {
        wireMock = WireMockServer().apply {
            start()
        }
        logger.info("WireMock started at ${wireMock.url("")}")
    }

    override fun afterEach(context: ExtensionContext?) {
        wireMock.stop()
        logger.info("WireMock stopped")
    }
}