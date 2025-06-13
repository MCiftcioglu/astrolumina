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

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = userRepository.getUserByEmailAndPassword(email, password)
            if (user != null) {
                loggedInUser = user
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }
}
