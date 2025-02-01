package com.numosophy.dao

import androidx.room.*
import com.numosophy.entity.User

@Dao
interface UserDao {
    // ✅ Insert a single user
    @Insert
    suspend fun insertUser(user: User)

    // ✅ Insert multiple users at once
    @Insert
    suspend fun insertUsers(users: List<User>)

    // ✅ Update user details
    @Update
    suspend fun updateUser(user: User)

    // ✅ Delete a specific user
    @Delete
    suspend fun deleteUser(user: User)

    // ✅ Delete all users
    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

    // ✅ Get all users
    @Query("SELECT * FROM users ORDER BY createdAt DESC")
    suspend fun getAllUsers(): List<User>

    // ✅ Get a user by ID
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Int): User?

    // ✅ Get users by role (e.g., all "Sales Rep")
    @Query("SELECT * FROM users WHERE role = :userRole")
    suspend fun getUsersByRole(userRole: String): List<User>

    // ✅ Count total users
    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int
}
