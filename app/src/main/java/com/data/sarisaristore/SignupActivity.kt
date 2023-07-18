package com.data.sarisaristore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.data.sarisaristore.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding
    private lateinit var databaseHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        binding.btnCancelRegistration.setOnClickListener {
            finish()
        }

        binding.btnRegister.setOnClickListener {
            val uName = binding.txtName.text.toString()
            val uEmail = binding.txtEmailAdd.text.toString()
            val uUser = binding.txtUserName.text.toString()
            val uPass = binding.txtPassword.text.toString()
            val uConfirm = binding.txtConfirm.text.toString()

            var required = ""
            if (uName == "") required = "Please enter name"
            else if (uEmail == "") required = "Please enter email address"
            else if (uUser == "") required = "Please enter user name"
            else if (uPass == "") required = "Please enter password"
            else if (uPass.length < 8 ) required = "Password atleast 8 characters"
            else if (uPass.filter { it.isDigit() }.firstOrNull() == null) required = "Password must have 1 number"
            else if (uPass.filter { it.isLetter() }.filter { it.isUpperCase() }.firstOrNull() == null) required = "Password must have 1 upper case"
            else if (uPass.filter { it.isLetter() }.filter { it.isLowerCase() }.firstOrNull() == null) required = "Password must have 1 lower carse"
            else if (uConfirm == "") required = "Please enter confirm password"
            else if (uPass != uConfirm) required = "Invalid confirm password"

            if (required == "") {
                val newUser = User(0, uName, uEmail, uUser, uPass)
                val ifExist = databaseHelper.ifExists(uUser)
                if (!ifExist) {
                    addUser(newUser)
                    finish()
                } else Toast.makeText(applicationContext,"Sorry! user name exists.",Toast.LENGTH_SHORT).show()
            } else Toast.makeText(applicationContext,"$required. Thank you!",Toast.LENGTH_SHORT).show()
        }
    }

    private fun addUser(user: User){
        databaseHelper.insertUser(user)
        Toast.makeText(applicationContext,"Name ${user.name} with user name ${user.user_name} was regitered.",Toast.LENGTH_SHORT).show()
    }

}