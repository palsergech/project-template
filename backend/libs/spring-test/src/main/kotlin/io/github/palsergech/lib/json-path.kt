package io.github.palsergech.lib

import com.fasterxml.jackson.databind.JsonNode
import wiremock.com.jayway.jsonpath.JsonPath

fun JsonNode.toJsonPath() = JsonPath.parse(this.toString())
