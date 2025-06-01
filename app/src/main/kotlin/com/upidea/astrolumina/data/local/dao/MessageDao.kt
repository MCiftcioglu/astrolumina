package com.upidea.astrolumina.data.local.dao

import androidx.room.*
import com.upidea.astrolumina.data.local.entity.Message

@Dao
interface MessageDao {
    @Insert
    suspend fun insertMessage(message: Message)

    @Query("SELECT * FROM Message WHERE (sender = :user1 AND receiver = :user2) OR (sender = :user2 AND receiver = :user1) ORDER BY id ASC")
    suspend fun getChatMessages(user1: String, user2: String): List<Message>
}
