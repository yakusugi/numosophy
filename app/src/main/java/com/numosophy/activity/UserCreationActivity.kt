package com.numosophy.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.numosophy.R
import com.numosophy.viewmodel.UserViewModel
import androidx.lifecycle.Observer
import androidx.activity.viewModels

class UserCreationActivity : BaseActivity() {
    private val userViewModel: UserViewModel by viewModels()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_creation)

        val signInBtn: ImageButton = findViewById(R.id.user_creation_btn)
        val userName: EditText = findViewById(R.id.edit_text_creation_user_name)
        val passwordEdtTxt: EditText = findViewById(R.id.edit_text_creation_user_pw)
        val userRole: TextView = findViewById(R.id.edit_text_creation_role)
        val userGroupId: TextView = findViewById(R.id.edit_text_creation_group_id)

        signInBtn.setOnClickListener {
            val name = userName.text.toString()
            val password = passwordEdtTxt.text.toString()
            val role = userRole.text.toString()
            val groupId = userGroupId.text.toString()

            if (name.isNotEmpty() && password.isNotEmpty() && role.isNotEmpty() && groupId.isNotEmpty()) {
                userViewModel.createUser(name, password, role, groupId)
                Toast.makeText(this, "User Created", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}