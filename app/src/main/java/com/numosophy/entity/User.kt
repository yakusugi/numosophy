package com.numosphere.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.util.Base64

@Entity(tableName = "users")
data class User(
    @PrimaryKey val publicKey: String,  // Unique ID (Instead of auto-generated ID)
    val name: String,
    val password: String,  // Should be hashed before storing
    val role: String,  // "Admin", "Manager", "Sales Rep"
    val groupId: String,  // Group for P2P sales tracking
    val createdAt: Long = System.currentTimeMillis()
) {
    companion object {
        // Generate a unique Public-Private Key pair for a new user
        fun generateUserKeys(): Pair<String, String> {
            val keyPairGen = KeyPairGenerator.getInstance("RSA")
            keyPairGen.initialize(2048)
            val pair: KeyPair = keyPairGen.genKeyPair()
            val publicKey = Base64.getEncoder().encodeToString(pair.public.encoded)
            val privateKey = Base64.getEncoder().encodeToString(pair.private.encoded)
            return Pair(publicKey, privateKey)
        }
    }
}
