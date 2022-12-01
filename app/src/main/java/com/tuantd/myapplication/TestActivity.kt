package com.tuantd.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.tuantd.myapplication.databinding.ActivityTestBinding
import java.util.*
import kotlin.collections.ArrayList

class TestActivity : AppCompatActivity() {
    lateinit var binding : ActivityTestBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.freeOn.setOnClickListener {
            binding.freeOff.visibility = View.VISIBLE
            binding.freeOn.visibility = View.GONE
        }
        binding.freeOff.setOnClickListener {
            binding.freeOff.visibility = View.GONE
            binding.freeOn.visibility = View.VISIBLE

        }
        binding.imgFreeOn.setOnClickListener {
            binding.freeOff.visibility = View.VISIBLE
            binding.freeOn.visibility = View.GONE
        }
        binding.imgFreeOff.setOnClickListener {
            binding.freeOff.visibility = View.GONE
            binding.freeOn.visibility = View.VISIBLE

        }


    }

}