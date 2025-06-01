package com.upidea.astrolumina.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val password: String,
    val birthDate: String,
    val birthTime: String? = null,
    val birthPlace: String? = null,
    val gender: String,
    val sunSign: String = "Koç",       // EKLENDİ
    val isOnline: Boolean = false      // EKLENDİ
)
