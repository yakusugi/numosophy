package com.numosophy.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.numosophy.R

class LoginActivity : BaseActivity() {
    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val signInBtn: ImageButton = findViewById(R.id.login_btn)
        val emailEdtTxt: EditText = findViewById(R.id.edit_text_login_user_name)
        val passwordEdtTxt: EditText = findViewById(R.id.edit_text_login_user_pw)
        val createProfileTxt: TextView = findViewById(R.id.text_view_create_profile)
        val pwForgotText: TextView = findViewById(R.id.text_view_fortgot_pw)

        createProfileTxt.setOnClickListener{
            safeStartActivity(UserCreationActivity::class.java)
        }

    }
}