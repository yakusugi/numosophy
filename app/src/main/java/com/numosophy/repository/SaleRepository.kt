package com.numosophy.repository

import androidx.lifecycle.LiveData
import com.numosophy.dao.SalesDao
import com.numosophy.entity.Sale

class SaleRepository(private val salesDao: SalesDao) {

    suspend fun insertSale(sale: Sale) {
        salesDao.insertSale(sale)
    }

    suspend fun getSalesByGroup(groupId: String): List<Sale> {
        return salesDao.getSalesByGroup(groupId)
    }

    suspend fun getSalesByUser(publicKey: String): List<Sale> {
        return salesDao.getSalesByUser(publicKey)
    }

    suspend fun getSalesByDate(date: String, groupId: String): List<Sale> {
        return salesDao.getSalesByDate(date, groupId)
    }

    suspend fun deleteSale(sale: Sale) {
        salesDao.deleteSale(sale)
    }

    suspend fun deleteSalesByGroup(groupId: String) {
        salesDao.deleteSalesByGroup(groupId)
    }

    val allSales: LiveData<List<Sale>> = salesDao.getAllSales()
}
