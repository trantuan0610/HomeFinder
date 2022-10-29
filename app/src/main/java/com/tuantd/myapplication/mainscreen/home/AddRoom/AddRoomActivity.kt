package com.tuantd.myapplication.mainscreen.home.AddRoom

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.tuantd.myapplication.R
import com.tuantd.myapplication.databinding.ActivityAddRoomBinding
import com.tuantd.myapplication.mainscreen.home.Room
import java.util.*

class AddRoomActivity : AppCompatActivity() {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("Rooms")
    private lateinit var addRoomBinding: ActivityAddRoomBinding
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    var imageUri: Uri? = null
    val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    val storageReference: StorageReference = firebaseStorage.reference
    var wifi = 1
    var vesinh = 1
    var dieuhoa = 1
    var maygiat = 1
    var tudo = 1
    var giuxe = 1
    var bep = 1
    var tulanh = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addRoomBinding = ActivityAddRoomBinding.inflate(layoutInflater)
        setContentView(addRoomBinding.root)

        registerActivityForResult()
        addRoomBinding.imgChose.setOnClickListener {
            chooseImage()
        }

        addRoomBinding.tvPush.setOnClickListener {
            uploadPhoto()
        }

        addRoomBinding.tvHuy.setOnClickListener {
            val eBuilder = AlertDialog.Builder(this)
            eBuilder.setTitle("Back")
            eBuilder.setMessage("Are you sure you want Exit?")
            eBuilder.setPositiveButton("Yes"){
                Dialog,which->
                finish()
            }
            eBuilder.setNegativeButton("No"){
                Dialog,which->

            }
            val createBuild = eBuilder.create()
            createBuild.show()


        }
        //wifi
        addRoomBinding.wifiOn.setOnClickListener {
            wifi = 0
            addRoomBinding.wifiOn.visibility = View.GONE
            addRoomBinding.wifiOff.visibility = View.VISIBLE
        }

        addRoomBinding.wifiOff.setOnClickListener {
            wifi = 1
            addRoomBinding.wifiOn.visibility = View.VISIBLE
            addRoomBinding.wifiOff.visibility = View.GONE
        }

        //wc
        addRoomBinding.wcOn.setOnClickListener {
            vesinh = 0
            addRoomBinding.wcOn.visibility = View.GONE
            addRoomBinding.wcOff.visibility = View.VISIBLE
        }

        addRoomBinding.wcOff.setOnClickListener {
            vesinh = 1
            addRoomBinding.wcOff.visibility = View.GONE
            addRoomBinding.wcOn.visibility = View.VISIBLE
        }

        //tulanh
        addRoomBinding.fridgeOn.setOnClickListener {
            tulanh = 0
            addRoomBinding.fridgeOn.visibility = View.GONE
            addRoomBinding.fridgeOff.visibility = View.VISIBLE
        }

        addRoomBinding.fridgeOff.setOnClickListener {
            tulanh = 1
            addRoomBinding.fridgeOn.visibility = View.VISIBLE
            addRoomBinding.fridgeOff.visibility = View.GONE
        }

        //bep
        addRoomBinding.kitchenOn.setOnClickListener {
            bep = 0
            addRoomBinding.kitchenOff.visibility = View.VISIBLE
            addRoomBinding.kitchenOn.visibility = View.GONE

        }

        addRoomBinding.kitchenOff.setOnClickListener {
            bep = 1
            addRoomBinding.kitchenOff.visibility = View.GONE
            addRoomBinding.kitchenOn.visibility = View.VISIBLE
        }

        //giuxe
        addRoomBinding.parkingOff.setOnClickListener {
            giuxe = 1
            addRoomBinding.parkingOff.visibility = View.GONE
            addRoomBinding.parkingOn.visibility = View.VISIBLE
        }

        addRoomBinding.parkingOn.setOnClickListener {
            giuxe = 0
            addRoomBinding.parkingOff.visibility = View.VISIBLE
            addRoomBinding.parkingOn.visibility = View.GONE
        }

        //tudo
        addRoomBinding.freeOn.setOnClickListener {
            tudo = 0
            addRoomBinding.freeOff.visibility = View.VISIBLE
            addRoomBinding.freeOn.visibility = View.GONE
        }

        addRoomBinding.freeOff.setOnClickListener {
            tudo = 1
            addRoomBinding.freeOn.visibility = View.VISIBLE
            addRoomBinding.freeOff.visibility = View.GONE
        }

        //maygiat
        addRoomBinding.washingMachineOff.setOnClickListener {
            maygiat = 1
            addRoomBinding.washingMachineOff.visibility = View.GONE
            addRoomBinding.washingMachineOn.visibility = View.VISIBLE
        }

        addRoomBinding.washingMachineOn.setOnClickListener {
            maygiat = 0
            addRoomBinding.washingMachineOn.visibility = View.GONE
            addRoomBinding.washingMachineOff.visibility = View.VISIBLE
        }

        //dieuhoa
        addRoomBinding.airConditionalOff.setOnClickListener {
            dieuhoa = 1
            addRoomBinding.airConditionalOff.visibility = View.GONE
            addRoomBinding.airConditionalOn.visibility = View.VISIBLE
        }

        addRoomBinding.airConditionalOn.setOnClickListener {
            dieuhoa = 0
            addRoomBinding.airConditionalOn.visibility = View.GONE
            addRoomBinding.airConditionalOff.visibility = View.VISIBLE
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
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
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
                            Picasso.get().load(it).into(addRoomBinding.imgChose)
                        }
                    }
                })
    }

    private fun addRoomToDatabase(url: String) {
        val address = addRoomBinding.edtRoomAddress.text.toString()
        val area = addRoomBinding.edtArea.text.toString()
        val price = addRoomBinding.edtPrice.text.toString()
        val des = addRoomBinding.edtDes.text.toString()
        val name =addRoomBinding.edtName.text.toString()
        val phone = addRoomBinding.edtPhone.text.toString()
        val email = FirebaseAuth.getInstance().currentUser?.email


        val id: String = myReference.push().key.toString()

        val room = Room(
            id,
            email!!,
            address,
            url,
            price,
            area,
            des,
            name,
            phone,
            wifi.toString(),
            vesinh.toString(),
            tudo.toString(),
            tulanh.toString(),
            dieuhoa.toString(),
            maygiat.toString(),
            giuxe.toString(),
            bep.toString()
        )

        myReference.child(id).setValue(room).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                finish()
            } else {
//                Toast.makeText(
//                    this,
//                    task.exception.toString(),
//                    Toast.LENGTH_LONG
//                ).show()

            }
        }
    }

    fun uploadPhoto() {
        val imageName = UUID.randomUUID().toString()
        val imageReference = storageReference.child("images").child(imageName)

        imageUri?.let { uri ->

            imageReference.putFile(uri).addOnSuccessListener {

                // Toast.makeText(this, "Image uploaded", Toast.LENGTH_LONG).show()

                //download url
                val myUploadedImageReference = storageReference.child("images").child(imageName)

                myUploadedImageReference.downloadUrl.addOnSuccessListener { url ->

                    val imageURL = url.toString()

                    addRoomToDatabase(imageURL)

                }

            }.addOnFailureListener {

                // Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
}