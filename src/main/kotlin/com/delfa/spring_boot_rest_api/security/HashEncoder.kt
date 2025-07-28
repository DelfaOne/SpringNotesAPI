package com.delfa.spring_boot_rest_api.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class HashEncoder {
    private val bcrypt = BCryptPasswordEncoder()

    fun encode(rawPassword: String): String = bcrypt.encode(rawPassword)

    fun matches(rawPassword: String, encodedPassword: String): Boolean = bcrypt.matches(rawPassword, encodedPassword)
}