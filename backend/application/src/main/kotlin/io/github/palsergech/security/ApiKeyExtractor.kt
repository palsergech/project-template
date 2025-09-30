package io.github.palsergech.security

import jakarta.servlet.ServletRequest
import jakarta.servlet.http.HttpServletRequest

internal class ApiKeyExtractor(
    private val apiKeyHeader: String
) {

    fun extract(request: ServletRequest): String? {
        (request as? HttpServletRequest) ?: throw IllegalArgumentException("Expected HttpServletRequest, but got ${request.javaClass}")
        return request.getHeader(apiKeyHeader)
    }
}
