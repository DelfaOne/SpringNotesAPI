package com.delfa.spring_boot_rest_api.controllers.auth

import com.delfa.spring_boot_rest_api.controllers.auth.model.AuthRequest
import com.delfa.spring_boot_rest_api.controllers.auth.model.RefreshRequest
import com.delfa.spring_boot_rest_api.security.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/register")
    fun register(
        @RequestBody body: AuthRequest
    ) = authService.register(body.email, body.password)

    @PostMapping("/login")
    fun login(
        @RequestBody body: AuthRequest
    ): AuthService.TokenPair = authService.login(body.email, body.password)

    @PostMapping("/refresh")
    fun refresh(
        @RequestBody body: RefreshRequest
    ): AuthService.TokenPair = authService.refresh(body.refreshToken)
}