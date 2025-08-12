package com.delfa.spring_boot_rest_api.security

import jakarta.servlet.DispatcherType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter,
) {

    @Bean
    fun filterChain(
        httpSecurity: HttpSecurity
    ): SecurityFilterChain {
        return httpSecurity.csrf { csrf -> csrf.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth.
                requestMatchers("/auth/**")
                    .permitAll() // Allow unauthenticated access to /auth/**
                    .dispatcherTypeMatchers(
                        DispatcherType.ERROR, DispatcherType.FORWARD
                    )
                    .permitAll()
                    .anyRequest()
                    .authenticated() // Require authentication for all other requests
            }
            .exceptionHandling { configurer ->
                configurer.authenticationEntryPoint(
                    HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED) // Return 401 Unauthorized for unauthenticated requests
                )
            }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }
}