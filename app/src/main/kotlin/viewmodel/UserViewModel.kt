package com.upidea.astrolumina.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.upidea.astrolumina.data.local.entity.UserEntity
import com.upidea.astrolumina.data.local.AppDatabase // ✅ BU EKLENDİ
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = AppDatabase.getDatabase(application).userDao()

    fun insertUser(user: UserEntity, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.insertUser(user)
            onSuccess()
        }
    }
}
