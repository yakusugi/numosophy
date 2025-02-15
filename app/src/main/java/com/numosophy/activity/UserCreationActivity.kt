package com.numosophy.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.numosophy.R

class UserCreationActivity : BaseActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_creation)

        val signInBtn: ImageButton = findViewById(R.id.user_creation_btn)
        val userName: EditText = findViewById(R.id.edit_text_creation_user_name)
        val passwordEdtTxt: EditText = findViewById(R.id.edit_text_creation_user_pw)
        val userRole: TextView = findViewById(R.id.edit_text_creation_role)
        val userGroupId: TextView = findViewById(R.id.edit_text_creation_group_id)



        

    }
}