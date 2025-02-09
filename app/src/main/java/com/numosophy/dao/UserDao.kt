package com.numosophy.dao

import androidx.room.*
import com.numosophy.entity.User

@Dao
interface UserDao {
    // ✅ Insert new user
    @Insert
    suspend fun insertUser(user: User)

    // ✅ Get all users in the same group
    @Query("SELECT * FROM users WHERE groupId = :groupId")
    suspend fun getUsersByGroup(groupId: String): List<User>

    // ✅ Get a user by name & password (for login authentication)
    @Query("SELECT * FROM users WHERE name = :name AND password = :password LIMIT 1")
    suspend fun authenticateUser(name: String, password: String): User?

    // ✅ Get users by role within a group
    @Query("SELECT * FROM users WHERE role = :role AND groupId = :groupId")
    suspend fun getUsersByRole(role: String, groupId: String): List<User>

    // ✅ Delete all users in a group (only for Admins)
    @Query("DELETE FROM users WHERE groupId = :groupId")
    suspend fun deleteGroupUsers(groupId: String)

    // ✅ Delete a specific user
    @Delete
    suspend fun deleteUser(user: User)

    // ✅ Count total number of users
    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int
}
