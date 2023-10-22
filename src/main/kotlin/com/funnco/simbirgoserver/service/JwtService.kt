package com.funnco.simbirgoserver.service

import com.auth0.jwt.JWT
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.util.*

@Service
class AuthService {

    final val BASE_SALT =
        "LdepaQ4UYa4m2wsNSMCEeDCALyVbZ9jpGbrhG5fAprDGWCf68f9F5B32L4SDkWawjEbNVbexNuvdUVbKzcLExJZchSaF2MrLuqfKnyUy886gAadGtNvawxYBmQWTHtJMKwhL5GQzk9PEE5DDpZrqXUZ3ycjVRxsLtcgBW82KBvwgeR6H4GCFG6jdEXN7TeBSSstRPVD4xvZ9hgV8nLBGG73mApXB63YUHyg6K4cyVU3pKEqx4gWQ4ZxhhHBMce4d"
    final val TOKEN_BASE =
        "fY7syChN7sEz5DxVettAaPHzmF69mxxZ4FAShpaBkEL3MTTgHwDZ7egHrmnLmwQvjBH9HNSqaZ75bxMJc4CNnGZ3VZAg4hqzUnyZLtA7Gh6KYjseMLyXPFY9593bgvYWDZuRtAVBhBfHKRcNBm3GjjRCq95YBqUed59zqHRQSwwCWhMbYGuEW89Es483qzTSLmfzVa3Wd8uqcGtNjNh37S3SGyZ9pfCUqNhcH6uGUSBweR7mawhZzvQQSGfyXRqD"


    val logger = LoggerFactory.getLogger(this::class.java.simpleName)

    fun hashPassword(basePassword: String): String {
        return hashString(basePassword + hashString(BASE_SALT, "SHA-256"), "MD5")
    }

    fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }

    private fun hashString(input: String, type: String): String {
        val bytes = MessageDigest
            .getInstance(type)
            .digest(input.toByteArray())
        return bytes.toHexString()
    }

    fun validateToken(token: String): Boolean {
        val decodedToken = JWT.decode(token)
        return decodedToken.expiresAt.after(Date(System.currentTimeMillis())) && decodedToken.issuer=="auth_service"
    }

    fun getUserIdFromToken(token: String): String {
        val decodedToken = JWT.decode(token)
        return decodedToken.subject
    }
}