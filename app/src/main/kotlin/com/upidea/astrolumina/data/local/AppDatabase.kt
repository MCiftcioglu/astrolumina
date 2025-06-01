package com.upidea.astrolumina.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.upidea.astrolumina.data.local.dao.UserDao
import com.upidea.astrolumina.data.local.dao.MessageDao
import com.upidea.astrolumina.data.local.entity.UserEntity
import com.upidea.astrolumina.data.local.entity.Message

@Database(entities = [UserEntity::class, Message::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "astro_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
