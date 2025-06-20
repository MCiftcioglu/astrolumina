package com.upidea.astrolumina.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sender: String,
    val receiver: String,  // ← BU SATIR VAR MI?
    val content: String,
    val timestamp: Long
)

