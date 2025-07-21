package com.upidea.astrolumina.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upidea.astrolumina.data.repository.UserRepository
import com.upidea.astrolumina.data.local.entity.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var loggedInUser: UserEntity? = null
        private set

    fun saveUserToLocalDb(user: UserEntity) {
        viewModelScope.launch {
            userRepository.insertUser(user)
        }
    }
}

