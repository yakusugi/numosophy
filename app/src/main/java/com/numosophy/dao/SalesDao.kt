package com.numosophy.dao

import androidx.room.*
import com.numosophy.entity.Sale

@Dao
interface SalesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(sale: Sale)

    @Query("SELECT * FROM sales WHERE groupId = :groupId ORDER BY timestamp DESC")
    suspend fun getSalesByGroup(groupId: String): List<Sale>

    @Query("SELECT * FROM sales WHERE createdBy = :publicKey")
    suspend fun getSalesByUser(publicKey: String): List<Sale>

    @Query("SELECT * FROM sales WHERE date = :date AND groupId = :groupId")
    suspend fun getSalesByDate(date: String, groupId: String): List<Sale>

    @Delete
    suspend fun deleteSale(sale: Sale)

    @Query("DELETE FROM sales WHERE groupId = :groupId")
    suspend fun deleteSalesByGroup(groupId: String)
}

