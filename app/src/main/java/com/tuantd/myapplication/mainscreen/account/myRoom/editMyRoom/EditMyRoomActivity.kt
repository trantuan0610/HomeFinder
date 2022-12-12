package com.tuantd.myapplication.mainscreen.account.myRoom.editMyRoom

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.tuantd.myapplication.R
import com.tuantd.myapplication.databinding.ActivityEditMyRoomBinding
import com.tuantd.myapplication.mainscreen.account.myRoom.DetailMyRoomActivity
import com.tuantd.myapplication.mainscreen.account.myRoom.MyRoomActivity
import com.tuantd.myapplication.mainscreen.home.Room
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EditMyRoomActivity : AppCompatActivity() {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("room")

    private lateinit var binding: ActivityEditMyRoomBinding
    lateinit var activityResultLauncher1: ActivityResultLauncher<Intent>

    var vitri = -1

    var listAnh = ArrayList<Any>()
    var listImageView = ArrayList<ImageView>()
    private var dialog: AlertDialog? = null

    var wifi = true
    var vesinh = true
    var dieuhoa = true
    var maygiat = true
    var tudo = true
    var giuxe = true
    var bep = true
    var tulanh = true
    lateinit var roomDetai: Room

    lateinit var adapterItemText : ArrayAdapter<String>
    var itemtext = arrayOf("Kí túc xá", "Phòng trọ và nhà trọ" , "Nhà Nguyên Căn" , "Tìm bạn ở ghép")
    var item = " "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditMyRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listImageView.add(binding.img1)
        listImageView.add(binding.img2)
        listImageView.add(binding.img3)
        listImageView.add(binding.img4)
        listImageView.add(binding.img5)
        listImageView.add(binding.img6)

         roomDetai = intent.getSerializableExtra("room") as Room
        //setdata
        setData(roomDetai)
        adapterItemText = ArrayAdapter(this, R.layout.item_text,itemtext)
        binding.autoTxt.setAdapter(adapterItemText)

        listAnh.forEachIndexed { index, any ->
            loadAnh(any,listImageView[index])
        }

        listImageView.forEachIndexed { index, imageView ->

            imageView.setOnClickListener {
                vitri = index
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
        }
        createDialog()

        binding.tvEdit.visibility = View.GONE
        binding.man2.visibility = View.GONE
        binding.man1.visibility =View.VISIBLE
        binding.tvTiep.setOnClickListener {
            binding.man1.visibility =View.GONE
            binding.man2.visibility = View.VISIBLE
            binding.tvTiep.visibility = View.GONE
            binding.tvEdit.visibility = View.VISIBLE
            binding.tvQuayLai.visibility = View.VISIBLE
            binding.tvHuy.visibility = View.GONE
        }
        binding.tvQuayLai.setOnClickListener {
            binding.man1.visibility =View.VISIBLE
            binding.man2.visibility = View.GONE
            binding.tvTiep.visibility = View.VISIBLE
            binding.tvEdit.visibility = View.GONE
            binding.tvQuayLai.visibility = View.GONE
            binding.tvHuy.visibility = View.VISIBLE
        }

        binding.tvEdit.setOnClickListener {
            showLoading()
            val listCheck = arrayListOf<Int>()
            listAnh.forEachIndexed{ index, imageView ->
                if (imageView.checkUri())
                   listCheck.add(index)
            }
            if (listCheck.isEmpty()){
               addRoomToDatabase()
            }
            listAnh.forEachIndexed { index, imageView ->
                if(listCheck.contains(index)){
                    val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
                    val storageReference: StorageReference = firebaseStorage.reference
                    val imageName = UUID.randomUUID().toString()
                    val imageReference = storageReference.child("images").child(imageName)
                    imageReference.putFile(imageView as Uri).addOnSuccessListener {
                        //download url
                        val myUploadedImageReference =
                            storageReference.child("images").child(imageName)

                        myUploadedImageReference.downloadUrl.addOnSuccessListener { url ->

                            listAnh[index] = url.toString()
                            listCheck.remove(index)
                            if (listCheck.isEmpty()){
                                addRoomToDatabase()
                            }
                        }
                    }
                }
            }
        }
        activityResultLauncher()
        binding.tvHuy.setOnClickListener {
            cancelAction()
        }
        setUtilities()



    }



    private fun setUtilities() {
        //wifi
        binding.wifiOn.setOnClickListener {
            wifi = false
            binding.wifiOn.visibility = View.GONE
            binding.wifiOff.visibility = View.VISIBLE
        }

        binding.wifiOff.setOnClickListener {
            wifi = true
            binding.wifiOn.visibility = View.VISIBLE
            binding.wifiOff.visibility = View.GONE
        }

        //wc
        binding.wcOn.setOnClickListener {
            vesinh = false
            binding.wcOn.visibility = View.GONE
            binding.wcOff.visibility = View.VISIBLE
        }

        binding.wcOff.setOnClickListener {
            vesinh = true
            binding.wcOff.visibility = View.GONE
            binding.wcOn.visibility = View.VISIBLE
        }

        //tulanh
        binding.fridgeOn.setOnClickListener {
            tulanh = false
            binding.fridgeOn.visibility = View.GONE
            binding.fridgeOff.visibility = View.VISIBLE
        }

        binding.fridgeOff.setOnClickListener {
            tulanh = true
            binding.fridgeOn.visibility = View.VISIBLE
            binding.fridgeOff.visibility = View.GONE
        }

        //bep
        binding.kitchenOn.setOnClickListener {
            bep = false
            binding.kitchenOff.visibility = View.VISIBLE
            binding.kitchenOn.visibility = View.GONE

        }

        binding.kitchenOff.setOnClickListener {
            bep = true
            binding.kitchenOff.visibility = View.GONE
            binding.kitchenOn.visibility = View.VISIBLE
        }

        //giuxe
        binding.parkingOff.setOnClickListener {
            giuxe = true
            binding.parkingOff.visibility = View.GONE
            binding.parkingOn.visibility = View.VISIBLE
        }

        binding.parkingOn.setOnClickListener {
            giuxe = false
            binding.parkingOff.visibility = View.VISIBLE
            binding.parkingOn.visibility = View.GONE
        }

        //tudo
        binding.freeOn.setOnClickListener {
            tudo = false
            binding.freeOff.visibility = View.VISIBLE
            binding.freeOn.visibility = View.GONE
        }

        binding.freeOff.setOnClickListener {
            tudo = true
            binding.freeOn.visibility = View.VISIBLE
            binding.freeOff.visibility = View.GONE
        }

        //maygiat
        binding.washingMachineOff.setOnClickListener {
            maygiat = true
            binding.washingMachineOff.visibility = View.GONE
            binding.washingMachineOn.visibility = View.VISIBLE
        }

        binding.washingMachineOn.setOnClickListener {
            maygiat = false
            binding.washingMachineOn.visibility = View.GONE
            binding.washingMachineOff.visibility = View.VISIBLE
        }

        //dieuhoa
        binding.airConditionalOff.setOnClickListener {
            dieuhoa = true
            binding.airConditionalOff.visibility = View.GONE
            binding.airConditionalOn.visibility = View.VISIBLE
        }

        binding.airConditionalOn.setOnClickListener {
            dieuhoa = false
            binding.airConditionalOn.visibility = View.GONE
            binding.airConditionalOff.visibility = View.VISIBLE
        }
    }

    fun activityResultLauncher() {
        activityResultLauncher1 =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
                ActivityResultCallback { result ->

                    val resultCode = result.resultCode
                    val imageData = result.data

                    if (resultCode == RESULT_OK && imageData != null) {
                        var  imageUri = imageData.data

                        imageUri?.let {
                            Glide.with(this).load(it).into(listImageView[vitri])
                            if (listAnh.size-1<vitri){
                                vitri=listAnh.size
                                listAnh.add(it)
                            }else{
                                listAnh[vitri] = it
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
    //clear
    fun Any.checkUri():Boolean{
        return this is Uri
    }

    private fun addRoomToDatabase() {

        val email = roomDetai.id_nguoi_dung.toString()
        val address = binding.edtRoomAddress.text.toString()
        val area = binding.edtArea.text.toString()
        val price = binding.edtPrice.text.toString()
        val des = binding.edtDes.text.toString()
        val name =binding.edtName.text.toString()
        val phone = binding.edtPhone.text.toString()
        val tieu_de= binding.edtRoomTitle.text.toString()
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        val date = Date()
        val current = formatter.format(date)

        val roomMap = mutableMapOf<String, Any>()
        roomMap["id_bai_dang"] = roomDetai.id_bai_dang
        roomMap["id_nguoi_dung"] = email
        roomMap["dia_chi"] = address
        roomMap["list_image"] = listAnh
        roomMap["gia"] = price
        roomMap["dien_tich"] = area
        roomMap["mo_ta"] = des
        roomMap["name"] = name
        roomMap["sdt"] = phone
        roomMap["wifi"] = wifi
        roomMap["nha_ve_sinh"] = vesinh
        roomMap["tu_do"] = tudo
        roomMap["tu_lanh"] = tulanh
        roomMap["dieu_hoa"] = dieuhoa
        roomMap["may_giat"] = maygiat
        roomMap["giu_xe"] = giuxe
        roomMap["bep_nau"] = bep
        roomMap["trang_thai_bai_dang"] = roomDetai.trang_thai_bai_dang
        roomMap["trang_thai_duyet"] = roomDetai.trang_thai_duyet
        roomMap["thoi_gian"] = current.toString()
        roomMap["tieu_de"] = tieu_de
        roomMap["id_loai_bai_dang"] = binding.autoTxt.text.toString()




        myReference.child(roomDetai.id_bai_dang).updateChildren(roomMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(this, DetailMyRoomActivity::class.java)
                intent.putExtra("roomId", roomDetai.id_bai_dang)
                startActivity(intent)
                hiddenLoading()
                finish()
            }
        }
    }


    private fun setData(roomDetai: Room) {
        binding.edtRoomAddress.setText(roomDetai.dia_chi)
        binding.edtArea.setText(roomDetai.dien_tich)
        binding.edtPrice.setText(roomDetai.gia)
        binding.edtDes.setText(roomDetai.mo_ta)
        binding.edtName.setText(roomDetai.name)
        binding.edtPhone.setText(roomDetai.sdt)
        binding.edtRoomTitle.setText(roomDetai.tieu_de)
        binding.autoTxt.setText(roomDetai.id_loai_bai_dang)
        listAnh.addAll(roomDetai.list_image as ArrayList<String>)

        listAnh.forEachIndexed { index, any ->
            vitri = index
            loadAnh(any,listImageView[index])
            listImageView[index].visibility = View.VISIBLE
            listImageView[index].setOnClickListener {
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
        }

        if (roomDetai.wifi == true) {
            wifi = true
            binding.wifiOn.visibility = View.VISIBLE
            binding.wifiOff.visibility = View.GONE
        } else {
            wifi = false
            binding.wifiOff.visibility = View.VISIBLE
            binding.wifiOn.visibility = View.GONE

        }
        if (roomDetai.nha_ve_sinh == true) {
            vesinh = true
            binding.wcOn.visibility = View.VISIBLE
            binding.wcOff.visibility = View.GONE
        } else {
            vesinh = false
            binding.wcOn.visibility = View.GONE
            binding.wcOff.visibility = View.VISIBLE

        }
        if (roomDetai.bep_nau == true) {
            bep = true
            binding.kitchenOn.visibility = View.VISIBLE
            binding.kitchenOff.visibility = View.GONE
        } else {
            bep = false
            binding.kitchenOff.visibility = View.VISIBLE
            binding.kitchenOn.visibility = View.GONE

        }
        if (roomDetai.giu_xe == true) {
            giuxe = true
            binding.parkingOn.visibility = View.VISIBLE
            binding.parkingOff.visibility = View.GONE
        } else {
            giuxe = false
            binding.parkingOff.visibility = View.VISIBLE
            binding.parkingOn.visibility = View.GONE

        }
        if (roomDetai.dieu_hoa == true) {
            dieuhoa = true
            binding.airConditionalOn.visibility = View.VISIBLE
            binding.airConditionalOff.visibility = View.GONE
        } else {
            dieuhoa = false
            binding.airConditionalOff.visibility = View.VISIBLE
            binding.airConditionalOn.visibility = View.GONE

        }
        if (roomDetai.tu_lanh == true) {
            tulanh = true
            binding.fridgeOn.visibility = View.VISIBLE
            binding.fridgeOff.visibility = View.GONE
        } else {
            tulanh = false
            binding.fridgeOff.visibility = View.VISIBLE
            binding.fridgeOn.visibility = View.GONE

        }
        if (roomDetai.tu_do == true) {
            tudo = true
            binding.freeOn.visibility = View.VISIBLE
            binding.freeOff.visibility = View.GONE
        } else {
            tudo = false
            binding.freeOff.visibility = View.VISIBLE
            binding.freeOn.visibility = View.GONE

        }
        if (roomDetai.may_giat == true) {
            maygiat = true
            binding.washingMachineOn.visibility = View.VISIBLE
            binding.washingMachineOff.visibility = View.GONE
        } else {
            maygiat = false
            binding.washingMachineOff.visibility = View.VISIBLE
            binding.washingMachineOn.visibility = View.GONE

        }


    }

    //loadAnh
    private fun loadAnh(img: Any, imageView: ImageView) {
        Glide.with(this).load(img).error(R.drawable.addanh).into(imageView)
    }
    private fun createDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.layout_loading, null)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        dialog = dialogBuilder.create()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(true)
    }

    fun showLoading() {
        dialog?.show()
    }

    fun hiddenLoading() {
        dialog?.dismiss()
    }
}