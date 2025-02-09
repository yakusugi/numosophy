package com.numosophy.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val password: String,
    val role: String, // "Admin", "Manager", "Sales Rep"
    val groupId: String,
    val createdAt: Long = System.currentTimeMillis()
)