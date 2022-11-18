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
import com.tuantd.myapplication.databinding.ActivityDetailMyRoomBinding
import com.tuantd.myapplication.dialog.DialogEditRoom
import com.tuantd.myapplication.mainscreen.account.myRoom.editMyRoom.EditMyRoomActivity
import com.tuantd.myapplication.mainscreen.home.AddRoom.AddRoomActivity
import com.tuantd.myapplication.mainscreen.home.DetailRoom.FavouriteRoom
import com.tuantd.myapplication.mainscreen.home.Room

class DetailMyRoomActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailMyRoomBinding
    private var roomList = ArrayList<Room>()
    private var roomFavList = ArrayList<FavouriteRoom>()
    private var likeList = ArrayList<FavouriteRoom>()
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
                if (it == 1) {
                val intent = Intent(this, EditMyRoomActivity::class.java).apply {
                    putExtra("room",roomDetail)
                    startActivity(this)
                }
                    finish()

                } else {
                    myReference.child(roomDetail!!.id_bai_dang).removeValue()
                    val intent = Intent(this, MyRoomActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this,"Đã xoá",Toast.LENGTH_SHORT).show()
                    finish()

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
                        wifi = room["wifi"] as String,
                        nha_ve_sinh = room["nha_ve_sinh"] as String,
                        tu_do = room["tu_do"] as String,
                        tu_lanh = room["tu_lanh"] as String,
                        dieu_hoa = room["dieu_hoa"] as String,
                        may_giat = room["may_giat"] as String,
                        giu_xe = room["giu_xe"] as String,
                        bep_nau = room["bep_nau"] as String,
                        trang_thai_bai_dang = room["trang_thai_bai_dang"] as String,
                        trang_thai_duyet = room["trang_thai_duyet"] as String,
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
                        Glide.with(applicationContext).load(it.list_image?.get(0)).into(binding.imgRoom)
                        if (it.wifi == "1") {
                            binding.wifiOn.visibility = View.VISIBLE
                            binding.wifiOff.visibility = View.GONE
                        } else {
                            binding.wifiOff.visibility = View.VISIBLE
                            binding.wifiOn.visibility = View.GONE

                        }
                        if (it.nha_ve_sinh == "1") {
                            binding.wcOn.visibility = View.VISIBLE
                            binding.wcOff.visibility = View.GONE
                        } else {
                            binding.wcOn.visibility = View.GONE
                            binding.wcOff.visibility = View.VISIBLE

                        }
                        if (it.bep_nau == "1") {
                            binding.kitchenOn.visibility = View.VISIBLE
                            binding.kitchenOff.visibility = View.GONE
                        } else {
                            binding.kitchenOff.visibility = View.VISIBLE
                            binding.kitchenOn.visibility = View.GONE

                        }
                        if (it.giu_xe == "1") {
                            binding.parkingOn.visibility = View.VISIBLE
                            binding.parkingOff.visibility = View.GONE
                        } else {
                            binding.parkingOff.visibility = View.VISIBLE
                            binding.parkingOn.visibility = View.GONE

                        }
                        if (it.dieu_hoa == "1") {
                            binding.airConditionalOn.visibility = View.VISIBLE
                            binding.airConditionalOff.visibility = View.GONE
                        } else {
                            binding.airConditionalOff.visibility = View.VISIBLE
                            binding.airConditionalOn.visibility = View.GONE

                        }
                        if (it.tu_lanh == "1") {
                            binding.fridgeOn.visibility = View.VISIBLE
                            binding.fridgeOff.visibility = View.GONE
                        } else {
                            binding.fridgeOff.visibility = View.VISIBLE
                            binding.fridgeOn.visibility = View.GONE

                        }
                        if (it.tu_do == "1") {
                            binding.freeOn.visibility = View.VISIBLE
                            binding.freeOff.visibility = View.GONE
                        } else {
                            binding.freeOff.visibility = View.VISIBLE
                            binding.freeOn.visibility = View.GONE

                        }
                        if (it.may_giat == "1") {
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
