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
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("Test")
    val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    val storageReference: StorageReference = firebaseStorage.reference
    val listUrl= arrayListOf<String>()
    private var listImage = mutableListOf<Uri>()
    lateinit var activityResultLauncher1: ActivityResultLauncher<Intent>
    lateinit var activityResultLauncher2: ActivityResultLauncher<Intent>
    lateinit var activityResultLauncher3: ActivityResultLauncher<Intent>
    lateinit var activityResultLauncher4: ActivityResultLauncher<Intent>
    lateinit var activityResultLauncher5: ActivityResultLauncher<Intent>
    lateinit var activityResultLauncher6: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerActivityForResult1()
        binding.tvContinue.setOnClickListener {
            xongbuoc1()
        }
        binding.back2.setOnClickListener {
            quaylaibuoc1()
        }
        binding.tvDone.setOnClickListener {
            uploadPhoto(listImage)
            finish()
        }

        binding.imgAnh1.setOnClickListener {
            chooseImageAnh1()
        }
        binding.imgAnh2.setOnClickListener {
            chooseImageAnh2()
        }
        binding.imgAnh3.setOnClickListener {
            chooseImageAnh3()
        }
        binding.imgAnh4.setOnClickListener {
            chooseImageAnh4()
        }
        binding.imgAnh5.setOnClickListener {
            chooseImageAnh5()
        }
        binding.imgAnh6.setOnClickListener {
            chooseImageAnh6()
        }

    }

    private fun chooseImageAnh6() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1
            )
        } else {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            activityResultLauncher6.launch(intent)
        }
    }

    private fun chooseImageAnh5() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1
            )
        } else {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            activityResultLauncher5.launch(intent)
        }
    }

    private fun chooseImageAnh4() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1
            )
        } else {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            activityResultLauncher4.launch(intent)
        }
    }

    private fun chooseImageAnh3() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1
            )
        } else {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            activityResultLauncher3.launch(intent)
        }
    }

    fun registerActivityForResult1(){
        activityResultLauncher1 =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
                ActivityResultCallback { result ->

                    val resultCode = result.resultCode
                    val imageData = result.data

                    if (resultCode == RESULT_OK && imageData != null) {
                        var  imageUri = imageData.data

                        imageUri?.let {
                            Picasso.get().load(it).into(binding.imgAnh1)
                        }
                        if (imageUri != null) {
                            listImage.add(imageUri)
                        }
                    }
                })
        //2
        activityResultLauncher2 =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
                ActivityResultCallback { result ->

                    val resultCode = result.resultCode
                    val imageData = result.data

                    if (resultCode == RESULT_OK && imageData != null) {
                        var  imageUri = imageData.data

                        imageUri?.let {
                            Picasso.get().load(it).into(binding.imgAnh2)
                        }
                        if (imageUri != null) {
                            listImage.add(imageUri)
                        }
                    }
                })
        //
        activityResultLauncher3 =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
                ActivityResultCallback { result ->

                    val resultCode = result.resultCode
                    val imageData = result.data

                    if (resultCode == RESULT_OK && imageData != null) {
                        var  imageUri = imageData.data

                        imageUri?.let {
                            Picasso.get().load(it).into(binding.imgAnh3)
                        }
                        if (imageUri != null) {
                            listImage.add(imageUri)
                        }
                    }
                })
        //
        activityResultLauncher4 =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
                ActivityResultCallback { result ->

                    val resultCode = result.resultCode
                    val imageData = result.data

                    if (resultCode == RESULT_OK && imageData != null) {
                        var  imageUri = imageData.data

                        imageUri?.let {
                            Picasso.get().load(it).into(binding.imgAnh4)
                        }
                        if (imageUri != null) {
                            listImage.add(imageUri)
                        }
                    }
                })
        //
        activityResultLauncher5 =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
                ActivityResultCallback { result ->

                    val resultCode = result.resultCode
                    val imageData = result.data

                    if (resultCode == RESULT_OK && imageData != null) {
                        var  imageUri = imageData.data

                        imageUri?.let {
                            Picasso.get().load(it).into(binding.imgAnh5)
                        }
                        if (imageUri != null) {
                            listImage.add(imageUri)
                        }
                    }
                })
        //
        activityResultLauncher6 =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
                ActivityResultCallback { result ->

                    val resultCode = result.resultCode
                    val imageData = result.data

                    if (resultCode == RESULT_OK && imageData != null) {
                        var  imageUri = imageData.data

                        imageUri?.let {
                            Picasso.get().load(it).into(binding.imgAnh6)
                        }
                        if (imageUri != null) {
                            listImage.add(imageUri)
                        }
                    }
                })
    }

    private fun chooseImageAnh1() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1
            )
        } else {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            activityResultLauncher1.launch(intent)
        }
    }
    private fun chooseImageAnh2() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1
            )
        } else {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            activityResultLauncher2.launch(intent)
        }
    }

    private fun quaylaibuoc1() {
        binding.edtName.visibility = View.VISIBLE
        binding.edtAddress.visibility = View.VISIBLE
        binding.back1.visibility = View.VISIBLE
        binding.back2.visibility =View.GONE
        binding.tvContinue.visibility = View.VISIBLE
        binding.tvDone.visibility = View.GONE
        binding.lloAddAnh.visibility = View.GONE
    }

    private fun xongbuoc1() {
        binding.edtName.visibility = View.GONE
        binding.edtAddress.visibility = View.GONE
        binding.back1.visibility =View.GONE
        binding.back2.visibility =View.VISIBLE
        binding.tvContinue.visibility = View.GONE
        binding.tvDone.visibility = View.VISIBLE
        binding.lloAddAnh.visibility = View.VISIBLE
    }

    fun uploadPhoto(listItemChoices: MutableList<Uri>){
        val listUrl= arrayListOf<String>()
        listItemChoices.forEachIndexed { index, uri ->
            val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
            val storageReference: StorageReference = firebaseStorage.reference
            val imageName = UUID.randomUUID().toString()
            val imageReference = storageReference.child("images").child(imageName)
            imageReference.putFile(uri).addOnSuccessListener {
                //download url
                val myUploadedImageReference = storageReference.child("images").child(imageName)

                myUploadedImageReference.downloadUrl.addOnSuccessListener { url ->

                    val imageURL = url.toString()

                    listUrl.add(imageURL)
                    if (index == listItemChoices.size-1){
                        addRoomToDatabase(listUrl)
                    }
                }

            }.addOnFailureListener {
                if (index == listItemChoices.size-1){
                    addRoomToDatabase(listUrl)
                }
            }
        }

    }

    private fun addRoomToDatabase(url: ArrayList<String>) {
        val id: String = myReference.push().key.toString()

        val test = Test(
            id,
            "",
            "tuan",
            url
        )

        myReference.child(id).setValue(test).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                finish()
            } else {
                Toast.makeText(
                    this,
                    task.exception.toString(),
                    Toast.LENGTH_LONG
                ).show()

            }
        }
    }

}