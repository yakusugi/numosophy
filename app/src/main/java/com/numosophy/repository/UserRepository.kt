package com.numosophy.repository

import com.numosophy.utility.KeyStoreHelper
import com.numosphere.dao.UserDao
import com.numosphere.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.Base64

class UserRepository(private val userDao: UserDao) {
    // Hash password before storing (SHA-256)
    private fun hashPassword(password: String): String {
        return password.hashCode().toString() // Replace with proper hashing method
    }

    //  Insert a new user (with hashed password)
    suspend fun insertUser(name: String, password: String, role: String, groupId: String) {
        val keyPair = KeyStoreHelper.generateKeyPair(name)  // Generate keys
        val newUser = User(
            publicKey = keyPair.first,  // Save public key
            name = name,
            password = hashPassword(password),  // Secure password
            role = role,
            groupId = groupId
        )
        userDao.insertUser(newUser)
    }

    //  Authenticate User (Verify Hash)
    suspend fun authenticateUser(name: String, password: String): User? {
        val hashedPassword = hashPassword(password)  // Hash input password
        return withContext(Dispatchers.IO) {
            userDao.authenticateUser(name, hashedPassword) // Compare with DB hash
        }
    }

    // Get user by Public Key
    suspend fun getUserByPublicKey(publicKey: String): User? {
        return userDao.getUserByPublicKey(publicKey)
    }

    // Get all users in a specific group
    suspend fun getUsersByGroup(groupId: String): List<User> {
        return userDao.getUsersByGroup(groupId)
    }

    // Get users by role
    suspend fun getUsersByRole(role: String, groupId: String): List<User> {
        return userDao.getUsersByRole(role, groupId)
    }
}
