package com.data.sarisaristore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.data.sarisaristore.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {
    private lateinit var binding : ActivitySplashScreenBinding
    private var SPLASH_DELAY : Long = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.Main).launch {
            delay(SPLASH_DELAY)
            var myIntent = Intent(this@SplashScreen, LoginActivity::class.java)
            startActivity(myIntent)
            finish()
        }
    }


}