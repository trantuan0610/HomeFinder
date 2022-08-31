package com.tuantd.myapplication.mainscreen.home.AddRoom

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.tuantd.myapplication.R
import com.tuantd.myapplication.mainscreen.home.Room
import java.util.*

class AddRoomActivity : AppCompatActivity() {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("Rooms")

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    var imageUri: Uri? = null

    val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    val storageReference: StorageReference = firebaseStorage.reference

    lateinit var btnAdd: Button
    lateinit var edtPrice: EditText
    lateinit var edtDes: EditText
    lateinit var edtArea: EditText
    lateinit var edtAddress: EditText
    lateinit var choseImg: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_room)

        btnAdd = findViewById(R.id.btnAddRoom)
        edtPrice = findViewById(R.id.edtPrice)
        edtDes = findViewById(R.id.edtDes)
        edtArea = findViewById(R.id.edtArea)
        edtAddress = findViewById(R.id.edtRoomAddress)
        choseImg = findViewById(R.id.imgChose)


        registerActivityForResult()
        choseImg.setOnClickListener {
            chooseImage()
        }

        btnAdd.setOnClickListener {
            uploadPhoto()
        }


    }

    private fun chooseImage() {
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
            intent.action = Intent.ACTION_GET_CONTENT
            activityResultLauncher.launch(intent)
        }
    }
    fun registerActivityForResult() {
        activityResultLauncher =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
                ActivityResultCallback { result ->

                    val resultCode = result.resultCode
                    val imageData = result.data

                    if (resultCode == RESULT_OK && imageData != null) {
                        imageUri = imageData.data

                        imageUri?.let {
                            Picasso.get().load(it).into(choseImg)
                        }
                    }
                })
    }

    private fun addRoomToDatabase(url: String) {
        val address: String = edtAddress.text.toString()
        val area: String = edtArea.text.toString()
        val price: String = edtPrice.text.toString()
        val des: String = edtDes.text.toString()

        val id: String = myReference.push().key.toString()

        val room = Room(id, address, url, price, area, des)

        myReference.child(id).setValue(room).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    this,
                    "The new user has been added to the database",
                    Toast.LENGTH_LONG
                ).show()
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
    fun uploadPhoto() {

        //UUID

        val imageName = UUID.randomUUID().toString()
        val imageReference = storageReference.child("images").child(imageName)

        imageUri?.let { uri ->

            imageReference.putFile(uri).addOnSuccessListener {

                Toast.makeText(this,"Image uploaded", Toast.LENGTH_LONG).show()

                //download url
                val myUploadedImageReference = storageReference.child("images").child(imageName)

                myUploadedImageReference.downloadUrl.addOnSuccessListener { url ->

                    val imageURL = url.toString()

                    addRoomToDatabase(imageURL)

                }

            }.addOnFailureListener {

                Toast.makeText(this,it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
}