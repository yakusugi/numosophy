package com.numosophy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.numosophy.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository

    init {
        val userDao = com.numosophy.utility.NumosophyDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
    }

    fun createUser(name: String, password: String, role: String, groupId: String) {
        viewModelScope.launch {
            repository.insertUser(name, password, role, groupId)
        }
    }
}
