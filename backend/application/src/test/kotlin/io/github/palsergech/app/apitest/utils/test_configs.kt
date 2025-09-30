package io.github.palsergech.app.apitest.utils

import com.fasterxml.jackson.databind.JsonNode

internal data class CatalogConfig(
    val catalogs: List<JsonNode>
)
