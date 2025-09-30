package io.github.palsergech.app.apitest.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

internal object TestConfigLoader {
    private val objectMapper = jacksonObjectMapper()

    inline fun <reified C : Any> loadFrom(path: String): C {
        return CatalogConfig::class.java.classLoader.getResourceAsStream(path)
            ?.use { stream ->
                objectMapper.readValue<C>(stream)
            }
            ?: error("resource not found: $path")
    }
}
