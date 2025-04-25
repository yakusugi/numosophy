package com.numosophy.utility

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.numosophy.dao.SalesDao
import com.numosophy.entity.Sale
import com.numosphere.dao.UserDao
import com.numosphere.entity.User

@Database(entities = [User::class, Sale::class], version = 5, exportSchema = false)
abstract class NumosophyDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun salesDao(): SalesDao

    companion object {
        @Volatile
        private var INSTANCE: NumosophyDatabase? = null

        fun getDatabase(context: Context): NumosophyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NumosophyDatabase::class.java,
                    "numosphere_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
