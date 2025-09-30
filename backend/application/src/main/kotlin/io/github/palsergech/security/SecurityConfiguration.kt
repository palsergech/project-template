package io.github.palsergech.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    @Value("\${cors.allowed-origins}")
    private val corsAllowedOrigins: String,
    @param:Value("\${security.api-key}")
    private val apiKey: String,
    @param:Value("\${security.api-key-header}")
    private val apiKeyHeader: String
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .cors {
                it.configurationSource(corsConfigurer())
            }
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/api/system/**","/openapi.yaml").permitAll()
                    .requestMatchers("/api/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            }
            .addFilterBefore(
                ApiKeyAuthenticationFilter(apiKey, ApiKeyExtractor(apiKeyHeader)),
                BearerTokenAuthenticationFilter::class.java
            )
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt(withDefaults())
            }
            .build()
    }

    private fun corsConfigurer() =  CorsConfigurationSource {
        CorsConfiguration().apply {
            allowedOriginPatterns = corsAllowedOrigins.split(",")
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
            allowCredentials = true
            allowedHeaders = listOf("*")
        }
    }
}