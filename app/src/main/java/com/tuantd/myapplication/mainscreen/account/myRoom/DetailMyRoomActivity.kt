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
            onBackPressed()
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
                    3-> {
                        val intent = Intent()
                        intent.action = Intent.ACTION_SEND
                        intent.putExtra(Intent.EXTRA_TEXT, "Địa chỉ:" + roomDetail?.dia_chi +"\n"+"Anh/chị:"+roomDetail?.name+"\n"+"SDT:"+roomDetail?.sdt)
                        intent.type = "text/plain"
                        startActivity(Intent.createChooser(intent, "Please select app: "))
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
                        binding.tvPrice.text = it.gia + "Đ"
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
                            binding.wifiOn.visibility = View.VISIBLE
                            binding.wifiOff.visibility = View.GONE
                        } else {
                            binding.wifiOff.visibility = View.VISIBLE
                            binding.wifiOn.visibility = View.GONE

                        }
                        if (it.nha_ve_sinh == true) {
                            binding.wcOn.visibility = View.VISIBLE
                            binding.wcOff.visibility = View.GONE
                        } else {
                            binding.wcOn.visibility = View.GONE
                            binding.wcOff.visibility = View.VISIBLE

                        }
                        if (it.bep_nau == true) {
                            binding.kitchenOn.visibility = View.VISIBLE
                            binding.kitchenOff.visibility = View.GONE
                        } else {
                            binding.kitchenOff.visibility = View.VISIBLE
                            binding.kitchenOn.visibility = View.GONE

                        }
                        if (it.giu_xe == true) {
                            binding.parkingOn.visibility = View.VISIBLE
                            binding.parkingOff.visibility = View.GONE
                        } else {
                            binding.parkingOff.visibility = View.VISIBLE
                            binding.parkingOn.visibility = View.GONE

                        }
                        if (it.dieu_hoa == true) {
                            binding.airConditionalOn.visibility = View.VISIBLE
                            binding.airConditionalOff.visibility = View.GONE
                        } else {
                            binding.airConditionalOff.visibility = View.VISIBLE
                            binding.airConditionalOn.visibility = View.GONE

                        }
                        if (it.tu_lanh == true) {
                            binding.fridgeOn.visibility = View.VISIBLE
                            binding.fridgeOff.visibility = View.GONE
                        } else {
                            binding.fridgeOff.visibility = View.VISIBLE
                            binding.fridgeOn.visibility = View.GONE

                        }
                        if (it.tu_do == true) {
                            binding.freeOn.visibility = View.VISIBLE
                            binding.freeOff.visibility = View.GONE
                        } else {
                            binding.freeOff.visibility = View.VISIBLE
                            binding.freeOn.visibility = View.GONE

                        }
                        if (it.may_giat == true) {
                            binding.washingMachineOn.visibility = View.VISIBLE
                            binding.washingMachineOff.visibility = View.GONE
                        } else {
                            binding.washingMachineOff.visibility = View.VISIBLE
                            binding.washingMachineOn.visibility = View.GONE

                        }

                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
