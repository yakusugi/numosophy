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

//        val db = NumosophyDatabase.getDatabase(this)
//        val userDao = db.userDao()

//        val button: Button = findViewById(R.id.addUserButton)
//        button.setOnClickListener {
//            CoroutineScope(Dispatchers.IO).launch {
//                // Insert a new user
//                val newUser = User(name = "John Doe", email = "john@example.com", role = "Admin")
//                userDao.insertUser(newUser)
//                Log.d("RoomTest", "User inserted!")
//
//                // Get all users
//                val userList = userDao.getAllUsers()
//                Log.d("RoomTest", "Users: $userList")
//            }
//        }
        val db = NumosophyDatabase.getDatabase(this)
        val userDao = db.userDao()

        // ✅ Run a simple query to confirm database creation
        CoroutineScope(Dispatchers.IO).launch {
            val userCount = userDao.getUserCount() // Query to check database existence
            Log.d("RoomTest", "Database initialized. Current users count: $userCount")
        }
    }
}