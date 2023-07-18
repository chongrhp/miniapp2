package com.data.sarisaristore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.data.sarisaristore.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var databaseHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        binding.btnLogin.setOnClickListener {
            val userName = binding.txtLoginUser.text.toString()
            val userPass = binding.txtLoginPassword.text.toString()

            var found = databaseHelper.loginUser(userName, userPass)
            if (found) {
                val myIntent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(myIntent)
                finish()
            } else Toast.makeText(applicationContext,"Wrong user name and password",Toast.LENGTH_SHORT).show()

        }

        binding.btnSignup.setOnClickListener {
            val myIntent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(myIntent)
        }
    }
}