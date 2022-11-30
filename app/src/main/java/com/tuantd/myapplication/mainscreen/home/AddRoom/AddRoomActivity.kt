package com.tuantd.myapplication.mainscreen.home.AddRoom

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.tuantd.myapplication.R
import com.tuantd.myapplication.databinding.ActivityAddRoomBinding
import com.tuantd.myapplication.dialog.LoadingDialog
import com.tuantd.myapplication.mainscreen.home.DetailRoom.FavouriteRoom
import com.tuantd.myapplication.mainscreen.home.Room
import java.text.SimpleDateFormat
import java.util.*

class AddRoomActivity : AppCompatActivity() {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("room")
    private val myUserReference: DatabaseReference = database.reference.child("user")
    private lateinit var binding: ActivityAddRoomBinding
    private var listImage = mutableListOf<Uri>()
    lateinit var activityResultLauncher1: ActivityResultLauncher<Intent>
    lateinit var activityResultLauncher2: ActivityResultLauncher<Intent>
    lateinit var activityResultLauncher3: ActivityResultLauncher<Intent>
    lateinit var activityResultLauncher4: ActivityResultLauncher<Intent>
    lateinit var activityResultLauncher5: ActivityResultLauncher<Intent>
    lateinit var activityResultLauncher6: ActivityResultLauncher<Intent>
    var wifi = false
    var vesinh = false
    var dieuhoa = false
    var maygiat = false
    var tudo = false
    var giuxe = false
    var bep = false
    var tulanh = false

    var userDetail : com.tuantd.myapplication.register.User ?= null
   var userList = ArrayList<com.tuantd.myapplication.register.User>()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    lateinit var adapterItemText : ArrayAdapter<String>
    var itemtext = arrayOf("Kí túc xá", "Phòng trọ và nhà trọ" , "Nhà Nguyên Căn" , "Tìm bạn ở ghép")
     var item = " "


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapterItemText = ArrayAdapter(this, R.layout.item_text,itemtext)
        binding.autoTxt.setAdapter(adapterItemText)

        createDialog()
        getDataUser()

        registerActivityForResult2()
        binding.man2.visibility = View.GONE
        binding.man1.visibility =View.VISIBLE
        binding.tvTiep.setOnClickListener {
            if(isValidateTitle()
                && isValidateArea()
                && isValidateAddress()
                &&isValidatePrice()
                &&isValidateContent()
                &&isValidateSDT()
                &&isValidateName()
                &&isValidateTypeRoom())
            {
                binding.man1.visibility =View.GONE
                binding.man2.visibility = View.VISIBLE
                binding.tvTiep.visibility = View.GONE
                binding.tvPush.visibility = View.VISIBLE
                binding.tvQuayLai.visibility = View.VISIBLE
                binding.tvHuy.visibility = View.GONE
            }

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

        setUtilities()
        binding.tvPush.setOnClickListener {
            if(isValidateImage()){
                showLoading()
                uploadPhoto(listImage)
            }

        }

        binding.tvHuy.setOnClickListener {
            cancelAction()
        }

    }


    private fun getDataUser() {
        myUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (user in snapshot.children) {
                    val uSer = user.value as? HashMap<*, *>
                    val data = com.tuantd.myapplication.register.User(
                        id_nguoi_dung =  uSer?.get("id_nguoi_dung") as String,
                        email = uSer["email"] as String,
                        mat_khau = uSer["mat_khau"] as String,
                        quyen = uSer["quyen"] as String,
                        sdt = uSer["sdt"] as String,
                        ten = uSer["ten"] as String
                    )
                    userList.add(data)
                }
                userList.forEach {
                    if (it.email == auth.currentUser?.email  ) {
                       userDetail = it
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
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
        eBuilder.setTitle("")
        eBuilder.setMessage("Bạn có chắc chắn muốn thoát? Những thay đổi của bạn sẽ không được lưu!")
        eBuilder.setPositiveButton("Đồng Ý"){
                Dialog,which->
            finish()
        }
        eBuilder.setNegativeButton("Huỷ"){
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
        val email = FirebaseAuth.getInstance().currentUser?.email
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        val date = Date()
        val current = formatter.format(date)

        val id: String = myReference.push().key.toString()

        val room = Room(
            id,
            userDetail?.id_nguoi_dung,
            address,
            url,
            price,
            area,
            des,
            name,
            phone,
            wifi,
            vesinh,
            tudo,
            tulanh,
            dieuhoa,
            maygiat,
            giuxe,
            bep,
            true,
            false,
            current.toString(),
            tieude,
            binding.autoTxt.text.toString()


        )

        myReference.child(id).setValue(room).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    this,
                    "Bạn đã đăng bài thành công. Hãy chờ ban quản trị duyệt bài. Nếu sau 24h chưa thấy duyệt, hãy liên hệ tới hotline 0852482628",
                    Toast.LENGTH_LONG
                ).show()
                hiddenLoading()
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

    //loading
    private var dialog: AlertDialog? = null

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
    //
    fun isValidateTitle() : Boolean{
        if (binding.edtRoomTitle.text.isEmpty()){
            binding.errorEmptyTitle.visibility = View.VISIBLE
            return false
        }
        binding.errorEmptyTitle.visibility = View.GONE
        return true
    }
    fun isValidateAddress() : Boolean{
        if (binding.edtRoomAddress.text.isEmpty()){
            binding.errorEmptyAddress.visibility = View.VISIBLE
            return false
        }
        binding.errorEmptyAddress.visibility = View.GONE
        return true
    }
    fun isValidatePrice() : Boolean{
        if (binding.edtPrice.text.isEmpty()){
            binding.errorEmptyPrice.visibility = View.VISIBLE
            return false
        }
        binding.errorEmptyPrice.visibility = View.GONE
        return true
    }
    fun isValidateArea() : Boolean{
        if (binding.edtArea.text.isEmpty()){
            binding.errorEmptyArea.visibility = View.VISIBLE
            return false
        }
        binding.errorEmptyArea.visibility = View.GONE
        return true
    }
    fun isValidateContent() : Boolean{
        if (binding.edtDes.text.isEmpty()){
            binding.errorEmptyContent.visibility = View.VISIBLE
            return false
        }
        binding.errorEmptyContent.visibility = View.GONE
        return true
    }
    fun isValidateName() : Boolean{
        if (binding.edtName.text.isEmpty()){
            binding.errorEmptyName.visibility = View.VISIBLE
            return false
        }
        binding.errorEmptyName.visibility = View.GONE
        return true
    }
    fun isValidateSDT() : Boolean{
        if (binding.edtPhone.text.isEmpty()){
            binding.errorEmptySDT.visibility = View.VISIBLE
            return false
        }
        binding.errorEmptySDT.visibility = View.GONE
        return true
    }
    fun isValidateTypeRoom() : Boolean{
        if (binding.autoTxt.text.isEmpty()){
            binding.errorEmptyTypeRoom.visibility = View.VISIBLE
            return false
        }
        binding.errorEmptyTypeRoom.visibility = View.GONE
        return true
    }
    private fun isValidateImage(): Boolean {
       if(listImage.isEmpty()){
           binding.errorEmptyImage.visibility = View.VISIBLE
           return false
       }
        binding.errorEmptyImage.visibility = View.GONE
        return true
    }
    override fun onBackPressed() {
        val eBuilder = AlertDialog.Builder(this)
        eBuilder.setTitle("")
        eBuilder.setMessage("Bạn có chắc chắn muốn thoát? Những thay đổi của bạn sẽ không được lưu!")
        eBuilder.setPositiveButton("Đồng ý"){
                Dialog,which->
            super.onBackPressed()
        }
        eBuilder.setNegativeButton("Huỷ"){
                Dialog,which->

        }
        val createBuild = eBuilder.create()
        createBuild.show()
    }
}