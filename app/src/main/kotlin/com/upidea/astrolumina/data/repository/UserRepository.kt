package com.upidea.astrolumina.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.upidea.astrolumina.data.local.dao.UserDao
import com.upidea.astrolumina.data.local.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val firestore: FirebaseFirestore
) {

    suspend fun insertUser(user: UserEntity) = withContext(Dispatchers.IO) {
        // Önce Firestore'a kaydet
        firestore.collection("users").document(user.uid).set(user).await()
        // Sonra yerel veritabanına kaydet
        userDao.insertUser(user)
    }

    suspend fun getUserByEmail(email: String): UserEntity? = withContext(Dispatchers.IO) {
        val querySnapshot = firestore.collection("users").whereEqualTo("email", email).limit(1).get().await()
        if (!querySnapshot.isEmpty) {
            return@withContext querySnapshot.documents[0].toObject(UserEntity::class.java)
        }
        return@withContext null
    }

    suspend fun getUserByEmailAndPassword(email: String, password: String): UserEntity? = withContext(Dispatchers.IO) {
        val querySnapshot = firestore.collection("users")
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .limit(1)
            .get()
            .await()
        if (!querySnapshot.isEmpty) {
            return@withContext querySnapshot.documents[0].toObject(UserEntity::class.java)
        }
        return@withContext null
    }

    suspend fun getAllUsers(): List<UserEntity> = withContext(Dispatchers.IO) {
        userDao.getAllUsers()
    }
}
