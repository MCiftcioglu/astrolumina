package com.upidea.astrolumina.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var uid: String = "", // Firebase UID için eklendi
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val birthDate: String = "",
    val birthTime: String? = null,
    val birthPlace: String? = null,
    val gender: String = "",
    val sunSign: String = "",
    val moonSign: String = "",
    val risingSign: String = "",
    val isOnline: Boolean = false
) {
    // Firestore'un toObject() metodu için gerekli olan boş constructor
    constructor() : this(0, "", "", "", "", "", null, null, "", "", "", "", false)
}
