package com.upidea.astrolumina.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.upidea.astrolumina.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = UserDatabase.getDatabase(application).userDao()

    fun insertUser(user: User, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.insertUser(user)
            onSuccess()
        }
    }
}
