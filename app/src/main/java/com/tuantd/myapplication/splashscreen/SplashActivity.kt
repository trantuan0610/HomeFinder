package com.tuantd.myapplication.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.tuantd.myapplication.R
import com.tuantd.myapplication.login.LoginActivity
import com.tuantd.myapplication.mainscreen.MainActivity

class SplashActivity : AppCompatActivity() {

    private var roomId = ""
    private var postId = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

            var uri = intent.data
        if (uri != null) {
          var path = uri.toString()
        }
            intent.extras?.let {
                for (key in it.keySet()) {
                    val value = intent.extras?.getString(key).toString()
                    if (key == "roomId"){
                        roomId = value
                    }else if (key == "postId"){
                        postId = value
                    }
                }
            }



        Handler(Looper.getMainLooper()).postDelayed({
            if (roomId == "" && postId == ""){
                var intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                startActivity(Intent(this,MainActivity::class.java).apply {
                    putExtra("roomId",roomId)
                    putExtra("postId",postId)
                })
                finish()
            }
        }, 1500)


    }
}