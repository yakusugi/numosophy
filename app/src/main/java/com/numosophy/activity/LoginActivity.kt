package com.numosophy.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.numosophy.R
import com.numosophy.viewmodel.UserViewModel

class LoginActivity : BaseActivity() {
    private val userViewModel: UserViewModel by viewModels()
    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val signInBtn: ImageButton = findViewById(R.id.login_btn)
        val loginUserNameEdtTxt: EditText = findViewById(R.id.edit_text_login_user_name)
        val passwordEdtTxt: EditText = findViewById(R.id.edit_text_login_user_pw)
        val createProfileTxt: TextView = findViewById(R.id.text_view_create_profile)
        val pwForgotText: TextView = findViewById(R.id.text_view_fortgot_pw)

        createProfileTxt.setOnClickListener{
            safeStartActivity(UserCreationActivity::class.java)
        }

        signInBtn.setOnClickListener {
            val email = loginUserNameEdtTxt.text.toString()
            val password = passwordEdtTxt.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                userViewModel.loginUser(email, password)
            } else {
                Toast.makeText(this, "Please enter credentials", Toast.LENGTH_SHORT).show()
            }
        }

        userViewModel.loginSuccess.observe(this, Observer { success ->
            if (success) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                // startActivity(Intent(this, DashboardActivity::class.java))
                // finish()
            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        })

    }
}