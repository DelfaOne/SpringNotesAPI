package com.delfa.spring_boot_rest_api.controllers.auth.model

data class AuthRequest(
    val email: String,
    val password: String
)

data class RefreshRequest(
    val refreshToken: String
)