package com.numosophy.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sales")
data class Sale(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Double,
    val buyerName: String,
    val buyerGender: String?,       // Optional
    val buyerBirthdate: String?,             // Optional
    val buyerLocation: String?,     // Optional
    val notes: String?,             // Optional
    val date: String,               //
    val timestamp: Long = System.currentTimeMillis(),
    val createdBy: String,          // publicKey of the user
    val groupId: String
)
