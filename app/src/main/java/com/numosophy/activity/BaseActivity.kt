package com.numosophy.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun safeStartActivity(targetActivity: Class<*>) {
        try {
            val intent = Intent(this, targetActivity)
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()  // Log the error for debugging
            Toast.makeText(this, "Error starting activity", Toast.LENGTH_SHORT).show()
        }
    }
}
