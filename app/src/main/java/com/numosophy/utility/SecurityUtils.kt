package com.numosophy.utility

import java.security.MessageDigest
import java.util.Base64

object SecurityUtils {
//    fun hashPassword(password: String): String {
//        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
//        return bytes.joinToString("") { "%02x".format(it) }
//    }

    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)
    }

    fun verifyPassword(input: String, hashed: String): Boolean {
        return hashPassword(input) == hashed
    }
}