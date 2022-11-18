package com.tuantd.myapplication.mainscreen.account.myRoom.editMyRoom

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.tuantd.myapplication.databinding.ActivityEditMyRoomBinding
import com.tuantd.myapplication.mainscreen.account.myRoom.MyRoomActivity
import com.tuantd.myapplication.mainscreen.home.Room
import java.util.*

class EditMyRoomActivity : AppCompatActivity() {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("Rooms")
    private lateinit var binding: ActivityEditMyRoomBinding
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
    lateinit var roomDetai: Room
    var imageURL : String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditMyRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
         roomDetai = intent.getSerializableExtra("room") as Room
        if (roomDetai != null) {
            setData(roomDetai)
        }
        registerActivityForResult()

        binding.imgChose.setOnClickListener {
            chooseImage()
        }

        binding.tvEdit.setOnClickListener {
            if(imageURL!= null){
                //addRoomToDatabase(imageURL!!)
            }else{
                addRoomToDatabase(roomDetai.list_image)
            }
        }

        binding.tvHuy.setOnClickListener {
            cancelAction()
        }
        setUtilities()



    }

    private fun setUtilities() {
        //wifi
        binding.wifiOn.setOnClickListener {
            wifi = 0
            binding.wifiOn.visibility = View.GONE
            binding.wifiOff.visibility = View.VISIBLE
        }

        binding.wifiOff.setOnClickListener {
            wifi = 1
            binding.wifiOn.visibility = View.VISIBLE
            binding.wifiOff.visibility = View.GONE
        }

        //wc
        binding.wcOn.setOnClickListener {
            vesinh = 0
            binding.wcOn.visibility = View.GONE
            binding.wcOff.visibility = View.VISIBLE
        }

        binding.wcOff.setOnClickListener {
            vesinh = 1
            binding.wcOff.visibility = View.GONE
            binding.wcOn.visibility = View.VISIBLE
        }

        //tulanh
        binding.fridgeOn.setOnClickListener {
            tulanh = 0
            binding.fridgeOn.visibility = View.GONE
            binding.fridgeOff.visibility = View.VISIBLE
        }

        binding.fridgeOff.setOnClickListener {
            tulanh = 1
            binding.fridgeOn.visibility = View.VISIBLE
            binding.fridgeOff.visibility = View.GONE
        }

        //bep
        binding.kitchenOn.setOnClickListener {
            bep = 0
            binding.kitchenOff.visibility = View.VISIBLE
            binding.kitchenOn.visibility = View.GONE

        }

        binding.kitchenOff.setOnClickListener {
            bep = 1
            binding.kitchenOff.visibility = View.GONE
            binding.kitchenOn.visibility = View.VISIBLE
        }

        //giuxe
        binding.parkingOff.setOnClickListener {
            giuxe = 1
            binding.parkingOff.visibility = View.GONE
            binding.parkingOn.visibility = View.VISIBLE
        }

        binding.parkingOn.setOnClickListener {
            giuxe = 0
            binding.parkingOff.visibility = View.VISIBLE
            binding.parkingOn.visibility = View.GONE
        }

        //tudo
        binding.freeOn.setOnClickListener {
            tudo = 0
            binding.freeOff.visibility = View.VISIBLE
            binding.freeOn.visibility = View.GONE
        }

        binding.freeOff.setOnClickListener {
            tudo = 1
            binding.freeOn.visibility = View.VISIBLE
            binding.freeOff.visibility = View.GONE
        }

        //maygiat
        binding.washingMachineOff.setOnClickListener {
            maygiat = 1
            binding.washingMachineOff.visibility = View.GONE
            binding.washingMachineOn.visibility = View.VISIBLE
        }

        binding.washingMachineOn.setOnClickListener {
            maygiat = 0
            binding.washingMachineOn.visibility = View.GONE
            binding.washingMachineOff.visibility = View.VISIBLE
        }

        //dieuhoa
        binding.airConditionalOff.setOnClickListener {
            dieuhoa = 1
            binding.airConditionalOff.visibility = View.GONE
            binding.airConditionalOn.visibility = View.VISIBLE
        }

        binding.airConditionalOn.setOnClickListener {
            dieuhoa = 0
            binding.airConditionalOn.visibility = View.GONE
            binding.airConditionalOff.visibility = View.VISIBLE
        }
    }

    private fun registerActivityForResult() {
        val imageName = UUID.randomUUID().toString()
        val imageReference = storageReference.child("images").child(imageName)
        activityResultLauncher =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
                ActivityResultCallback { result ->

                    val resultCode = result.resultCode
                    val imageData = result.data

                    if (resultCode == RESULT_OK && imageData != null) {
                        imageUri = imageData.data

                        imageUri?.let {
                            Picasso.get().load(it).into(binding.imgChose)
                        }
                        imageUri?.let { uri ->

                            imageReference.putFile(uri).addOnSuccessListener {
                                val myUploadedImageReference = storageReference.child("images").child(imageName)

                                myUploadedImageReference.downloadUrl.addOnSuccessListener { url ->
                                    imageURL = url.toString()

                                }

                            }.addOnFailureListener {

                            }
                        }
                    }
                })
    }

    private fun cancelAction() {
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

    private fun uploadPhoto(){
        val imageName = UUID.randomUUID().toString()
        val imageReference = storageReference.child("images").child(imageName)

        imageUri?.let { uri ->

            imageReference.putFile(uri).addOnSuccessListener {
                val myUploadedImageReference = storageReference.child("images").child(imageName)

                myUploadedImageReference.downloadUrl.addOnSuccessListener { url ->
                     imageURL = url.toString()

                }

            }.addOnFailureListener {

            }
        }
    }

    private fun addRoomToDatabase(url: List<String>?) {
        val id = roomDetai.id_bai_dang
        val email = FirebaseAuth.getInstance().currentUser?.email
        val address = binding.edtRoomAddress.text.toString()
        val area = binding.edtArea.text.toString()
        val price = binding.edtPrice.text.toString()
        val des = binding.edtDes.text.toString()
        val name =binding.edtName.text.toString()
        val phone = binding.edtPhone.text.toString()

        val roomMap = mutableMapOf<String, Any>()
        roomMap["id_bai_dang"] = id
        roomMap["id_nguoi_dung"] = email.toString()
        roomMap["dia_chi"] = address
        //roomMap["list_image"] = url
        roomMap["gia"] = price
        roomMap["dien_tich"] = area
        roomMap["mo_ta"] = des
        roomMap["name"] = name
        roomMap["sdt"] = phone
        roomMap["wifi"] = wifi.toString()
        roomMap["nha_ve_sinh"] = vesinh.toString()
        roomMap["tu_do"] = tudo.toString()
        roomMap["tu_lanh"] = tulanh.toString()
        roomMap["dieu_hoa"] = dieuhoa.toString()
        roomMap["may_giat"] = maygiat.toString()
        roomMap["giu_xe"] = giuxe.toString()
        roomMap["bep_nau"] = bep.toString()
        roomMap["trang_thai_bai_dang"] = bep.toString()
        roomMap["trang_thai_duyet"] = bep.toString()
        roomMap["thoi_gian"] = bep.toString()
        roomMap["nong_lanh"] = bep.toString()
        roomMap["tieu_de"] = bep.toString()
        roomMap["id_loai_bai_dang"] = bep.toString()




        myReference.child(id).updateChildren(roomMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(this, MyRoomActivity::class.java)
                startActivity(intent)
                finish()
            }
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

    private fun setData(roomDetai: Room) {
        binding.edtRoomAddress.setText(roomDetai.dia_chi)
        binding.edtArea.setText(roomDetai.dien_tich)
        binding.edtPrice.setText(roomDetai.gia)
        binding.edtDes.setText(roomDetai.mo_ta)
        binding.edtName.setText(roomDetai.name)
        binding.edtPhone.setText(roomDetai.sdt)
        roomDetai.list_image?.get(0).let {
            Picasso.get().load(it).into(binding.imgChose)
        }

        if (roomDetai.wifi == "1") {
            wifi = 1
            binding.wifiOn.visibility = View.VISIBLE
            binding.wifiOff.visibility = View.GONE
        } else {
            wifi = 0
            binding.wifiOff.visibility = View.VISIBLE
            binding.wifiOn.visibility = View.GONE

        }
        if (roomDetai.nha_ve_sinh == "1") {
            vesinh = 1
            binding.wcOn.visibility = View.VISIBLE
            binding.wcOff.visibility = View.GONE
        } else {
            vesinh = 0
            binding.wcOn.visibility = View.GONE
            binding.wcOff.visibility = View.VISIBLE

        }
        if (roomDetai.bep_nau == "1") {
            bep = 1
            binding.kitchenOn.visibility = View.VISIBLE
            binding.kitchenOff.visibility = View.GONE
        } else {
            bep = 0
            binding.kitchenOff.visibility = View.VISIBLE
            binding.kitchenOn.visibility = View.GONE

        }
        if (roomDetai.giu_xe == "1") {
            giuxe = 1
            binding.parkingOn.visibility = View.VISIBLE
            binding.parkingOff.visibility = View.GONE
        } else {
            giuxe = 0
            binding.parkingOff.visibility = View.VISIBLE
            binding.parkingOn.visibility = View.GONE

        }
        if (roomDetai.dieu_hoa == "1") {
            dieuhoa = 1
            binding.airConditionalOn.visibility = View.VISIBLE
            binding.airConditionalOff.visibility = View.GONE
        } else {
            dieuhoa = 0
            binding.airConditionalOff.visibility = View.VISIBLE
            binding.airConditionalOn.visibility = View.GONE

        }
        if (roomDetai.tu_lanh == "1") {
            tulanh = 1
            binding.fridgeOn.visibility = View.VISIBLE
            binding.fridgeOff.visibility = View.GONE
        } else {
            tulanh = 0
            binding.fridgeOff.visibility = View.VISIBLE
            binding.fridgeOn.visibility = View.GONE

        }
        if (roomDetai.tu_do == "1") {
            tudo = 1
            binding.freeOn.visibility = View.VISIBLE
            binding.freeOff.visibility = View.GONE
        } else {
            tudo = 0
            binding.freeOff.visibility = View.VISIBLE
            binding.freeOn.visibility = View.GONE

        }
        if (roomDetai.may_giat == "1") {
            maygiat = 1
            binding.washingMachineOn.visibility = View.VISIBLE
            binding.washingMachineOff.visibility = View.GONE
        } else {
            maygiat = 0
            binding.washingMachineOff.visibility = View.VISIBLE
            binding.washingMachineOn.visibility = View.GONE

        }


    }
}