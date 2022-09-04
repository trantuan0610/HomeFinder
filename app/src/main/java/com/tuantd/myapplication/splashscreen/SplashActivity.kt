package com.tuantd.myapplication.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.tuantd.myapplication.R
import com.tuantd.myapplication.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            var intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 1500)
    }
}