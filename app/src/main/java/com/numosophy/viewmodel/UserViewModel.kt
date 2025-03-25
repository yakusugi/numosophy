package com.numosophy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.numosophy.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository
    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> get() = _loginSuccess

    init {
        val userDao = com.numosophy.utility.NumosophyDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
    }

    fun createUser(name: String, password: String, role: String, groupId: String) {
        viewModelScope.launch {
            repository.insertUser(name, password, role, groupId)
        }
    }

    fun loginUser(name: String, password: String) {
        viewModelScope.launch {
            val isAuthenticated = repository.authenticateUser(name, password)
            _loginSuccess.postValue(isAuthenticated)
        }
    }

}
