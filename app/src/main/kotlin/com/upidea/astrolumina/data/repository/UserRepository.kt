package com.upidea.astrolumina.data.repository

import com.upidea.astrolumina.data.local.entity.UserEntity
import com.upidea.astrolumina.data.local.dao.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject // ✅ Hilt için gerekli

class UserRepository @Inject constructor( // ✅ Hilt'in oluşturabilmesi için eklenen anotasyon
    private val userDao: UserDao
) {

    suspend fun insertUser(user: UserEntity) = withContext(Dispatchers.IO) {
        userDao.insertUser(user)
    }

    suspend fun getUserByEmail(email: String): UserEntity? = withContext(Dispatchers.IO) {
        userDao.getUserByEmail(email)
    }

    suspend fun getUserByEmailAndPassword(email: String, password: String): UserEntity? = withContext(Dispatchers.IO) {
        userDao.getUserByEmailAndPassword(email, password)
    }

    suspend fun getAllUsers(): List<UserEntity> = withContext(Dispatchers.IO) {
        userDao.getAllUsers()
    }
}
