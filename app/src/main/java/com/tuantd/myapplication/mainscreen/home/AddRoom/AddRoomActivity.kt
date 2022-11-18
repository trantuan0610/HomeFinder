package com.tuantd.myapplication.mainscreen.home.AddRoom

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
import com.tuantd.myapplication.databinding.ActivityAddRoomBinding
import com.tuantd.myapplication.mainscreen.home.Room
import java.text.SimpleDateFormat
import java.util.*

class AddRoomActivity : AppCompatActivity() {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("room")
    private lateinit var binding: ActivityAddRoomBinding
    val listUrl= arrayListOf<String>()
    private var listImage = mutableListOf<Uri>()
    lateinit var activityResultLauncher1: ActivityResultLauncher<Intent>
    lateinit var activityResultLauncher2: ActivityResultLauncher<Intent>
    lateinit var activityResultLauncher3: ActivityResultLauncher<Intent>
    lateinit var activityResultLauncher4: ActivityResultLauncher<Intent>
    lateinit var activityResultLauncher5: ActivityResultLauncher<Intent>
    lateinit var activityResultLauncher6: ActivityResultLauncher<Intent>
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

        binding = ActivityAddRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerActivityForResult2()
        binding.man2.visibility = View.GONE
        binding.tvTiep.setOnClickListener {
            binding.man1.visibility =View.GONE
            binding.man2.visibility = View.VISIBLE
            binding.tvTiep.visibility = View.GONE
            binding.tvPush.visibility = View.VISIBLE
            binding.tvQuayLai.visibility = View.VISIBLE
            binding.tvHuy.visibility = View.GONE
        }
        binding.tvQuayLai.setOnClickListener {
            binding.man1.visibility =View.VISIBLE
            binding.man2.visibility = View.GONE
            binding.tvTiep.visibility = View.VISIBLE
            binding.tvPush.visibility = View.GONE
            binding.tvQuayLai.visibility = View.GONE
            binding.tvHuy.visibility = View.VISIBLE
        }
        binding.img1.setOnClickListener {
            chooseImageAnh1()
        }
        binding.img2.setOnClickListener {
            chooseImageAnh2()
        }
        binding.img3.setOnClickListener {
            chooseImageAnh3()
        }
        binding.img4.setOnClickListener {
            chooseImageAnh4()
        }
        binding.img5.setOnClickListener {
            chooseImageAnh5()
        }
        binding.img6.setOnClickListener {
            chooseImageAnh6()
        }

        binding.tvPush.setOnClickListener {
            uploadPhoto(listImage)
        }

        binding.tvHuy.setOnClickListener {
            cancelAction()
        }
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

    private fun cancelAction(){
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

    fun registerActivityForResult2() {
        activityResultLauncher1 =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
                ActivityResultCallback { result ->

                    val resultCode = result.resultCode
                    val imageData = result.data

                    if (resultCode == RESULT_OK && imageData != null) {
                        var  imageUri = imageData.data

                        imageUri?.let {
                            Picasso.get().load(it).into(binding.img1)
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
                            Picasso.get().load(it).into(binding.img2)
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
                            Picasso.get().load(it).into(binding.img3)
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
                            Picasso.get().load(it).into(binding.img4)
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
                            Picasso.get().load(it).into(binding.img5)
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
                            Picasso.get().load(it).into(binding.img6)
                        }
                        if (imageUri != null) {
                            listImage.add(imageUri)
                        }
                    }
                })
    }

    private fun addRoomToDatabase(url: ArrayList<String>) {
        val address = binding.edtRoomAddress.text.toString()
        val area = binding.edtArea.text.toString()
        val price = binding.edtPrice.text.toString()
        val des = binding.edtDes.text.toString()
        val name =binding.edtName.text.toString()
        val phone = binding.edtPhone.text.toString()
        val tieude = binding.edtRoomTitle.text.toString()
        val id_nguoi_dung = FirebaseAuth.getInstance().currentUser?.email
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        val date = Date()
        val current = formatter.format(date)

        val id: String = myReference.push().key.toString()

        val room = Room(
            id,
            id_nguoi_dung!!,
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
            bep.toString(),
            "1",
            "0",
            current.toString(),
            tieude,
            "1"


        )

        myReference.child(id).setValue(room).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    this,
                    "Bạn đã đăng bài thành công. Hãy chờ ban quản trị duyệt bài. Nếu sau 24h chưa thấy duyệt, hãy liên hệ tới hotline 0852482628",
                    Toast.LENGTH_LONG
                ).show()
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

    override fun onBackPressed() {
        val eBuilder = AlertDialog.Builder(this)
        eBuilder.setTitle("Back")
        eBuilder.setMessage("Are you sure you want Exit?")
        eBuilder.setPositiveButton("Yes"){
                Dialog,which->
            super.onBackPressed()
        }
        eBuilder.setNegativeButton("No"){
                Dialog,which->

        }
        val createBuild = eBuilder.create()
        createBuild.show()
    }
}