package com.delfa.spring_boot_rest_api.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService(
    @Value("\${jwt.secret}") private val jwtSecret: String
) {

    private val secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret))

    companion object {
        const val ACCESS_TOKEN_VALIDITY_MS = 15 * 60 * 1000L       // 15 minutes
        const val REFRESH_TOKEN_VALIDITY_MS = 30L * 24 * 60 * 60 * 1000 // 30 days
    }

    fun generateAccessToken(userId: String): String = generateToken(userId, "access", ACCESS_TOKEN_VALIDITY_MS)

    fun generateRefreshToken(userId: String): String = generateToken(userId, "refresh", REFRESH_TOKEN_VALIDITY_MS)

    fun validateAccessToken(token: String): Boolean = validateToken(token, "access")

    fun validateRefreshToken(token: String): Boolean = validateToken(token, "refresh")

    //Authorization: Bearer <token>
    fun getUserIdFromToken(token: String): String {
        val claims = parseAllClaims(token) ?: throw IllegalArgumentException("Invalid token")
        return claims.subject ?: throw IllegalArgumentException("Token does not contain user ID")
    }

    private fun generateToken(userId: String, type: String, expiry: Long): String {
        val now = Date()
        return Jwts.builder().apply {
            subject(userId)
            claim("type", type)
            issuedAt(now)
            expiration(Date(now.time + expiry))
            signWith(secretKey, Jwts.SIG.HS256)
        }.compact()
    }

    //Return payload of the token
    private fun parseAllClaims(token: String): Claims? {
        val rawToken = if (token.startsWith("Bearer ")) {
            token.removePrefix("Bearer ")
        } else token
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(rawToken)
                .payload
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun validateToken(token: String, expectedType: String): Boolean {
        val tokenType = (parseAllClaims(token)?.get("type") as? String) ?: return false
        return tokenType == expectedType
    }
}