package io.github.palsergech.security

import io.github.palsergech.lib.platform.domain.DomainError
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.ClaimAccessor
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.util.*

internal object AuthExtractor {

    fun getCurrentUserId(): UUID {
        val context = SecurityContextHolder.getContext()
        if (context.authentication == null || !context.authentication.isAuthenticated) {
            throw AuthenticationServiceException("User is not authenticated")
        }

        when (val authentication = context.authentication) {
            is JwtAuthenticationToken -> {
                val claimAccessor = authentication.principal as? ClaimAccessor
                    ?: throw AuthenticationServiceException("Principal is not JWT principal")
                return UUID.fromString(claimAccessor.getClaim("sub"))
            }
            is ApiKeyAuthentication -> {
                throw DomainError("Admin has no user id")
            }
            else -> throw AuthenticationServiceException("Unexpected authentication type: ${context.authentication::class.java}")
        }
    }
}
