package com.tuantd.myapplication.mainscreen.account.term

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tuantd.myapplication.databinding.ActivityTermBinding

class TermActivity : AppCompatActivity() {
    lateinit var binding : ActivityTermBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTermBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}