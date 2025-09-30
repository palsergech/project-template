package io.github.palsergech.lib.spring.persistence.jdbc

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.postgresql.util.PGobject

const val PG_JSON_TYPE = "jsonb"

fun <T> T.toPgObject(objectMapper: ObjectMapper = jacksonObjectMapper()) = PGobject().also {
    it.type = PG_JSON_TYPE
    it.value = objectMapper.writeValueAsString(this)
}

inline fun <reified T> PGobject.parseOrNull(objectMapper: ObjectMapper): T? {
    val v = value ?: return null
    return objectMapper.readValue<T>(v)
}

inline fun <reified T> PGobject.parse(objectMapper: ObjectMapper): T {
    val v = value ?: error("unexpected null object instead of json")
    return objectMapper.readValue<T>(v)
}