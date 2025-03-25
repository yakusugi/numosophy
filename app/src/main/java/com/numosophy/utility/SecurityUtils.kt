package com.numosophy.utility

import java.security.MessageDigest

object SecurityUtils {
    fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun verifyPassword(input: String, hashed: String): Boolean {
        return hashPassword(input) == hashed
    }
}