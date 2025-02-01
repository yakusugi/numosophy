package com.numosophy.utility

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.numosophy.dao.UserDao
import com.numosophy.entity.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class NumosophyDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: NumosophyDatabase? = null

        fun getDatabase(context: Context): NumosophyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NumosophyDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}