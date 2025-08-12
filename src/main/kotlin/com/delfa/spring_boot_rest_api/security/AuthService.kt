package com.delfa.spring_boot_rest_api.security

import com.delfa.spring_boot_rest_api.database.model.RefreshToken
import com.delfa.spring_boot_rest_api.database.model.User
import com.delfa.spring_boot_rest_api.database.repository.RefreshTokenRepository
import com.delfa.spring_boot_rest_api.database.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.security.MessageDigest
import java.time.Instant
import java.util.Base64

@Service
class AuthService(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val hashEncoder: HashEncoder,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    data class TokenPair(
        val accessToken: String,
        val refreshToken: String
    )

    fun register(email: String, password: String): User {
        userRepository.findByEmail(email.trim())?.let {
            throw ResponseStatusException(HttpStatus.CONFLICT, "User with this email already exists.")
        }
        return userRepository.save(User(email = email, hashedPassword = hashEncoder.encode(password)))
    }

    fun login(email: String, password: String): TokenPair {
        val user = userRepository.findByEmail(email)
            ?: throw BadCredentialsException("Invalid credentials.")

        if (!hashEncoder.matches(password, user.hashedPassword)) {
            throw BadCredentialsException("Invalid credentials.")
        }

        val newAccessToken = jwtService.generateAccessToken(user.id.toHexString())
        val newRefreshToken = jwtService.generateRefreshToken(user.id.toHexString())

        storeRefreshToken(user.id, newRefreshToken)

        return TokenPair(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }

    @Transactional //Ensure atomicity
    fun refresh(refreshToken: String): TokenPair {
        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw ResponseStatusException(
                HttpStatusCode.valueOf(401),
                "Invalid refresh token."
            )
        }

        val userId = jwtService.getUserIdFromToken(refreshToken)
        val user = userRepository.findById(ObjectId(userId))
            .orElseThrow {
                ResponseStatusException(
                    HttpStatusCode.valueOf(401),
                    "Invalid refresh token."
                )
            }

        hashToken(refreshToken).also { hashedToken ->
            refreshTokenRepository.findByUserIdAndHashedToken(user.id, hashedToken)
                ?: throw ResponseStatusException(
                    HttpStatusCode.valueOf(401),
                    "Refresh token not recognized (maybe used or expired)."
                )

            refreshTokenRepository.deleteByUserIdAndHashedToken(user.id, hashedToken)
        }

        val (newAccessToken, newRefreshToken) = jwtService.run {
            generateAccessToken(userId) to generateRefreshToken(userId)
        }
        storeRefreshToken(user.id, newRefreshToken)

        return TokenPair(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }

    private fun storeRefreshToken(userId: ObjectId, rawRefreshToken: String) {
        val hashedToken = hashToken(rawRefreshToken)
        val expiryMs = JwtService.REFRESH_TOKEN_VALIDITY_MS
        val expiresAt = Instant.now().plusMillis(expiryMs)

        refreshTokenRepository.save(
            RefreshToken(
                userId = userId,
                hashedToken = hashedToken,
                expiresAt = expiresAt
            )
        )
    }

    private fun hashToken(rawToken: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashedBytes = digest.digest(rawToken.toByteArray())
        return Base64.getEncoder().encodeToString(hashedBytes)
    }
}