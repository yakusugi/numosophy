package com.numosophy.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.numosophy.entity.Sale
import com.numosophy.repository.SaleRepository
import com.numosophy.utility.NumosophyDatabase
import kotlinx.coroutines.launch

class SaleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SaleRepository

    init {
        val salesDao = NumosophyDatabase.getDatabase(application).salesDao()
        repository = SaleRepository(salesDao)
    }

    val allSales: LiveData<List<Sale>> = repository.allSales


    fun insertSale(sale: Sale) {
        viewModelScope.launch {
            repository.insertSale(sale)
        }
    }

    suspend fun getSalesByGroup(groupId: String) = repository.getSalesByGroup(groupId)
    suspend fun getSalesByUser(publicKey: String) = repository.getSalesByUser(publicKey)
    suspend fun getSalesByDate(date: String, groupId: String) = repository.getSalesByDate(date, groupId)
    fun deleteSale(sale: Sale) {
        viewModelScope.launch {
            repository.deleteSale(sale)
        }
    }
    fun deleteSalesByGroup(groupId: String) {
        viewModelScope.launch {
            repository.deleteSalesByGroup(groupId)
        }
    }
}
