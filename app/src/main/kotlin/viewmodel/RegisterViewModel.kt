package com.upidea.astrolumina.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upidea.astrolumina.data.entity.UserEntity
import com.upidea.astrolumina.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun registerUser(user: UserEntity) {
        viewModelScope.launch {
            userRepository.insertUser(user)
        }
    }
}
