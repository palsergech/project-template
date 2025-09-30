package io.github.palsergech.security

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean


internal class ApiKeyAuthenticationFilter(
    private val apiKey: String,
    private val apiKeyExtractor: ApiKeyExtractor
) : GenericFilterBean() {

    override fun doFilter(request: ServletRequest, response: ServletResponse, filterChain: FilterChain) {
        val extractedApiKey = apiKeyExtractor.extract(request)
        when(extractedApiKey) {
            apiKey -> {
                SecurityContextHolder.getContext().authentication =
                    ApiKeyAuthentication(apiKey, AuthorityUtils.createAuthorityList("ROLE_ADMIN"))
            }
            else -> { /* do nothing */ }
        }
        filterChain.doFilter(request, response)
    }
}
