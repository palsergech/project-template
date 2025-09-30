package io.github.palsergech.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority


internal class ApiKeyAuthentication(
    private val apiKey: String?,
    authorities: Collection<GrantedAuthority?>?
): AbstractAuthenticationToken(authorities) {

    init {
        isAuthenticated = true
    }

    override fun getCredentials(): Any? = null

    override fun getPrincipal(): Any? = apiKey
}
