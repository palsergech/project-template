package io.github.palsergech.app.apitest.utils

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import java.time.Instant


@Configuration
class ApplicationTestConfiguration {

    @Bean
    @Primary
    fun jwtDecoder() = JwtDecoder { token ->
        val issuedAt = Instant.now().minusSeconds(3600)
        val expiresAt = Instant.now().plusSeconds(3600)
        val headers = mapOf("a" to "b")
        val claims = mapOf("sub" to token)
        Jwt(token, issuedAt, expiresAt, headers, claims)
    }
}