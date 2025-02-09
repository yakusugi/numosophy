package com.numosophy.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.numosophy.R
import com.numosophy.entity.User
import com.numosophy.utility.NumosophyDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = NumosophyDatabase.getDatabase(this)
        val userDao = db.userDao()

        // ✅ Run a simple query to confirm database creation
        CoroutineScope(Dispatchers.IO).launch {
            val userCount = userDao.getUserCount() // Query to check database existence
            Log.d("RoomTest", "Database initialized. Current users count: $userCount")
        }
    }
}