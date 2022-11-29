package com.tuantd.myapplication.mainscreen.account.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tuantd.myapplication.databinding.ActivityChangeProfileBinding

class ChangeProfileActivity : AppCompatActivity() {
    lateinit var binding : ActivityChangeProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}