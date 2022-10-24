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

//    lateinit var edtPrice: EditText
//    lateinit var edtDes: EditText
//    lateinit var edtArea: EditText
//    lateinit var edtAddress: EditText
//    lateinit var edtName: EditText
//    lateinit var edtPhone: EditText
//    lateinit var choseImg: ImageView
//    lateinit var tvadd: TextView
//    lateinit var tvHuy: TextView
//
//    lateinit var btn_wifiOn: ImageButton
//    lateinit var btn_wifiOf: ImageButton
    var wifi = 0

//    lateinit var btn_vesinhOn: ImageButton
//    lateinit var btn_vesinhOf: ImageButton
    var vesinh = 0

//    lateinit var btn_dieuhoaOn: ImageButton
//    lateinit var btn_dieuhoaOf: ImageButton
    var dieuhoa = 0

//    lateinit var btn_maygiatOn: ImageButton
//    lateinit var btn_maygiatOf: ImageButton
    var maygiat = 0

//    lateinit var btn_tudoOf: ImageButton
//    lateinit var btn_tudoOn: ImageButton
    var tudo = 0

//    lateinit var btn_giuxeOf: ImageButton
//    lateinit var btn_giuxeOn: ImageButton
    var giuxe = 0

//    lateinit var btn_bepOf: ImageButton
//    lateinit var btn_bepOn: ImageButton
    var bep = 0

//    lateinit var btn_tulanhOn: ImageButton
//    lateinit var btn_tulanhOf: ImageButton
    var tulanh = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addRoomBinding = ActivityAddRoomBinding.inflate(layoutInflater)
        setContentView(addRoomBinding.root)

//        edtPrice = findViewById(R.id.edtPrice)
//        edtDes = findViewById(R.id.edtDes)
//        edtArea = findViewById(R.id.edtArea)
//        edtAddress = findViewById(R.id.edtRoomAddress)
//        choseImg = findViewById(R.id.imgChose)
//        edtName = findViewById(R.id.edtName)
//        edtPhone = findViewById(R.id.edtPhone)
//        tvadd = findViewById(R.id.tvPush)
//        tvHuy = findViewById(R.id.tvHuy)
//
//        btn_wifiOn = findViewById(R.id.wifi_on)
//        btn_wifiOf = findViewById(R.id.wifi_off)
//        btn_vesinhOn = findViewById(R.id.wc_on)
//        btn_vesinhOf = findViewById(R.id.wc_off)
//        btn_dieuhoaOn = findViewById(R.id.airConditional_on)
//        btn_dieuhoaOf = findViewById(R.id.airConditional_off)
//        btn_maygiatOn = findViewById(R.id.washingMachine_on)
//        btn_maygiatOf = findViewById(R.id.washingMachine_off)
//        btn_tudoOf = findViewById(R.id.free_off)
//        btn_tudoOn = findViewById(R.id.free_on)
//        btn_giuxeOf = findViewById(R.id.parking_off)
//        btn_giuxeOn = findViewById(R.id.parking_on)
//        btn_bepOf = findViewById(R.id.kitchen_off)
//        btn_bepOn = findViewById(R.id.kitchen_on)
//        btn_tulanhOf = findViewById(R.id.fridge_off)
//        btn_tulanhOn = findViewById(R.id.fridge_on)

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

        addRoomBinding.wifiOn.setOnClickListener {
            wifi = 0
            addRoomBinding.wifiOn.visibility = View.GONE
            addRoomBinding.wifiOff.visibility = View.VISIBLE
            Log.e("Tuan","1")
        }

        addRoomBinding.wifiOff.setOnClickListener {
            wifi = 1
            addRoomBinding.wifiOn.visibility = View.VISIBLE
            addRoomBinding.wifiOff.visibility = View.GONE
        }

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

        addRoomBinding.fridgeOff.setOnClickListener {
            tudo = 1
            addRoomBinding.fridgeOff.visibility = View.GONE
            addRoomBinding.fridgeOn.visibility = View.VISIBLE
        }

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


        val id: String = myReference.push().key.toString()

        val room = Room(
            id,
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
                //Toast.makeText(this,"The new user has been added to the database",Toast.LENGTH_LONG).show()
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

        //UUID

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