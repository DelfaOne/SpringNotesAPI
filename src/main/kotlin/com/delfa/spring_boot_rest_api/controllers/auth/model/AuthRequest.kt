package com.delfa.spring_boot_rest_api.controllers.auth.model

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern

data class AuthRequest(
    @field:Email(message = "Invalid email format")
    val email: String,
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{9,}\$",
        message = "Password must be at least 9 characters long and contain at least one digit, uppercase and lowercase character."
    )
    val password: String
)

data class RefreshRequest(
    val refreshToken: String
)