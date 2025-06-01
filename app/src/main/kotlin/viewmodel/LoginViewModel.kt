package com.upidea.astrolumina.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.upidea.astrolumina.data.entity.UserEntity
import com.upidea.astrolumina.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun getUserByEmail(email: String): LiveData<UserEntity?> {
        return userRepository.getUserByEmail(email)
    }
}
