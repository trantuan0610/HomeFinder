package com.tuantd.myapplication.mainscreen.account.myRoom

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tuantd.myapplication.ImageAdapter
import com.tuantd.myapplication.databinding.ActivityDetailMyRoomBinding
import com.tuantd.myapplication.dialog.DialogEditRoom
import com.tuantd.myapplication.dialog.DialogRate
import com.tuantd.myapplication.mainscreen.account.myRoom.editMyRoom.EditMyRoomActivity
import com.tuantd.myapplication.mainscreen.home.AddRoom.AddRoomActivity
import com.tuantd.myapplication.mainscreen.home.DetailRoom.FavouriteRoom
import com.tuantd.myapplication.mainscreen.home.Report.ReportActivity
import com.tuantd.myapplication.mainscreen.home.Room
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class DetailMyRoomActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailMyRoomBinding
    private var roomList = ArrayList<Room>()
    private var roomFavList = ArrayList<FavouriteRoom>()
    private var likeList = ArrayList<FavouriteRoom>()
    private val imageAdapter = ImageAdapter()
    private var roomDetail: Room? = null
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("room")
    private val myFavouriteReferene: DatabaseReference = database.reference.child("favourite")
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMyRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
//            onBackPressed()
            startActivity(Intent(this,MyRoomActivity::class.java))
            finish()
        }
        binding.share.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, "Bài Đăng từ HOMEFINDER"+"\n"+"Địa chỉ: " + roomDetail?.dia_chi +"\n"+"Anh/chị: "+roomDetail?.name+"\n"+"SDT: "+roomDetail?.sdt)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Please select app: "))
        }

        val roomId = intent.getStringExtra("roomId")
        if (roomId != null) {
            retrieveDataFromDatabase(roomId)
        }
        binding.btnMore.setOnClickListener {
            DialogEditRoom(onSubmitClickListener = { it ->
                when(it){
                    1 -> {
                        val intent = Intent(this, EditMyRoomActivity::class.java).apply {
                            putExtra("room", roomDetail)
                            startActivity(this)
                            finish()
                        }
                    }

                    2-> {
                        myReference.child(roomDetail!!.id_bai_dang).removeValue()
                        val intent = Intent(this, MyRoomActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this,"Đã xoá",Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    4-> {
                        myReference.child(roomDetail!!.id_bai_dang).child("trang_thai_bai_dang").setValue(true)
                        Toast.makeText(this,"Đã thay đổi trạng thái",Toast.LENGTH_SHORT).show()


                    }
                    5-> {
                        myReference.child(roomDetail!!.id_bai_dang).child("trang_thai_bai_dang").setValue(false)
                        Toast.makeText(this,"Đã thay đổi trạng thái",Toast.LENGTH_SHORT).show()
                    }
                }

            }).show(supportFragmentManager, "tag")
        }
    }
    private fun setTextMoney(text: String) {
        val symbols = DecimalFormatSymbols()
        symbols.setDecimalSeparator(',')
        val decimalFormat = DecimalFormat("###,###,###,###Đ", symbols)
        binding.tvPrice.setText(decimalFormat.format(text.toInt()))
    }
    private fun retrieveDataFromDatabase(roomID: String) {
        myReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                roomList.clear()
                for (eachRoom in snapshot.children) {
                    val room = eachRoom.value as? HashMap<*, *>
                    val data = Room(
                        id_bai_dang = room?.get("id_bai_dang") as String,
                        id_nguoi_dung = room["id_nguoi_dung"] as String,
                        dia_chi = room["dia_chi"] as String,
                        list_image = room["list_image"] as List<String>,
                        gia = room["gia"] as String,
                        dien_tich = room["dien_tich"] as String,
                        mo_ta = room["mo_ta"] as String,
                        name = room["name"] as String,
                        sdt = room["sdt"] as String,
                        wifi = room["wifi"] as Boolean,
                        nha_ve_sinh = room["nha_ve_sinh"] as Boolean,
                        tu_do = room["tu_do"] as Boolean,
                        tu_lanh = room["tu_lanh"] as Boolean,
                        dieu_hoa = room["dieu_hoa"] as Boolean,
                        may_giat = room["may_giat"] as Boolean,
                        giu_xe = room["giu_xe"] as Boolean,
                        bep_nau = room["bep_nau"] as Boolean,
                        trang_thai_bai_dang = room["trang_thai_bai_dang"] as Boolean,
                        trang_thai_duyet = room["trang_thai_duyet"] as Boolean,
                        thoi_gian = room["thoi_gian"] as String,
                        tieu_de = room["tieu_de"] as String,
                        id_loai_bai_dang = room["id_loai_bai_dang"] as String
                    )
                    roomList.add(data)
                }
                roomList.forEach {
                    if (it.id_bai_dang.equals(roomID)) {
                        roomDetail = it
                        binding.tvRoomAddress.text = it.dia_chi
//                        binding.tvPrice.text = it.gia + "Đ"
                        setTextMoney(it.gia)
                        binding.tvRoomArea.text = it.dien_tich + "m2"
                        binding.tvDetailDes.text = it.mo_ta
                        binding.tvtitle.text = it.tieu_de
                        binding.tvName.text = it.name + "-" + it.sdt
                        binding.tvDate.text = "Ngày đăng: " + it.thoi_gian
                        binding.tvLoaiPhong.text = "Loại Phòng: " + it.id_loai_bai_dang

                        if(it.trang_thai_bai_dang == true){
                            binding.tvShow.visibility = View.VISIBLE
                            binding.tvHide.visibility = View.GONE
                        }else{
                            binding.tvShow.visibility = View.GONE
                            binding.tvHide.visibility = View.VISIBLE
                        }

                        imageAdapter.addList(it?.list_image as ArrayList<String>)
                        binding.rcvImage.adapter = imageAdapter

                        if (it.wifi == true) {
                            binding.ltWifiOn.visibility = View.VISIBLE
                            binding.ltWifiOff.visibility = View.GONE
                        } else {
                            binding.ltWifiOff.visibility = View.VISIBLE
                            binding.ltWifiOn.visibility = View.GONE

                        }
                        if (it.nha_ve_sinh == true) {
                            binding.ltWcOn.visibility = View.VISIBLE
                            binding.ltWcOff.visibility = View.GONE
                        } else {
                            binding.ltWcOn.visibility = View.GONE
                            binding.ltWcOff.visibility = View.VISIBLE

                        }
                        if (it.bep_nau == true) {
                            //
                            binding.ltKitchenOn.visibility = View.VISIBLE
                            binding.ltKichenOff.visibility = View.GONE
                        } else {
                            binding.ltKichenOff.visibility = View.VISIBLE
                            binding.ltKitchenOn.visibility = View.GONE

                        }
                        if (it.giu_xe == true) {
                            binding.ltParkingOn.visibility = View.VISIBLE
                            binding.ltParkingOff.visibility = View.GONE
                        } else {
                            binding.ltParkingOff.visibility = View.VISIBLE
                            binding.ltParkingOn.visibility = View.GONE

                        }
                        if (it.dieu_hoa == true) {
                            binding.ltAirConditionalOn.visibility = View.VISIBLE
                            binding.ltAirConditionalOff.visibility = View.GONE
                        } else {
                            binding.ltAirConditionalOff.visibility = View.VISIBLE
                            binding.ltAirConditionalOn.visibility = View.GONE

                        }
                        if (it.tu_lanh == true) {
                            binding.ltFridgeOn.visibility = View.VISIBLE
                            binding.ltFridgeOff.visibility = View.GONE
                        } else {
                            binding.ltFridgeOff.visibility = View.VISIBLE
                            binding.ltFridgeOn.visibility = View.GONE

                        }
                        if (it.tu_do == true) {
                            binding.ltFreeOn.visibility = View.VISIBLE
                            binding.ltFreeOff.visibility = View.GONE
                        } else {
                            binding.ltFreeOff.visibility = View.VISIBLE
                            binding.ltFreeOn.visibility = View.GONE

                        }
                        if (it.may_giat == true) {
                            binding.ltWashingMachineOn.visibility = View.VISIBLE
                            binding.ltWashingMachineOff.visibility = View.GONE
                        } else {
                            binding.ltWashingMachineOff.visibility = View.VISIBLE
                            binding.ltWashingMachineOn.visibility = View.GONE

                        }

                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun onBackPressed() {
//        setResult(RESULT_OK,Intent())
//        super.onBackPressed()
        startActivity(Intent(this,MyRoomActivity::class.java))
        finish()
    }
}
