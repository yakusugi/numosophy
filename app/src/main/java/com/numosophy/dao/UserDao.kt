package com.numosphere.dao

import androidx.room.*
import com.numosphere.entity.User

@Dao
interface UserDao {
    //  Insert a new user (Replaces if the publicKey already exists)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE name = :name")
    suspend fun getUserByName(name: String): User?


    //  Get all users in the same group
    @Query("SELECT * FROM users WHERE groupId = :groupId")
    suspend fun getUsersByGroup(groupId: String): List<User>

    //  Compare hashed password
    @Query("SELECT * FROM users WHERE name = :name AND password = :password LIMIT 1")
    suspend fun authenticateUser(name: String, password: String): User?

    //  Get a user by publicKey (More secure than using name & password)
    @Query("SELECT * FROM users WHERE publicKey = :publicKey LIMIT 1")
    suspend fun getUserByPublicKey(publicKey: String): User?

    //  Get users by role within a group
    @Query("SELECT * FROM users WHERE role = :role AND groupId = :groupId")
    suspend fun getUsersByRole(role: String, groupId: String): List<User>

    //  Delete all users in a group (Only for Admins)
    @Query("DELETE FROM users WHERE groupId = :groupId")
    suspend fun deleteGroupUsers(groupId: String)

    //  Delete a specific user
    @Delete
    suspend fun deleteUser(user: User)

    //  Count total number of users
    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int


}
