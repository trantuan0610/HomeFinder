package com.tuantd.myapplication.mainscreen.home.KtxRoom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tuantd.myapplication.R
import com.tuantd.myapplication.databinding.ActivityListKtxRoomBinding
import com.tuantd.myapplication.databinding.ActivityListMotelBinding
import com.tuantd.myapplication.mainscreen.home.DetailRoom.DetailRoomActivity
import com.tuantd.myapplication.mainscreen.home.Room
import com.tuantd.myapplication.mainscreen.home.RoomsAdapter
import com.tuantd.myapplication.mainscreen.home.RoomsAdapter2
import com.tuantd.myapplication.mainscreen.home.RoomsAdapter3

class ListKtxRoomActivity : AppCompatActivity() {
    lateinit var binding : ActivityListKtxRoomBinding

    private var loadDone:(() -> Unit)?=null
    private var getRoomList = ArrayList<Room>()
    private var roomList = ArrayList<Room>()
    private var  roomListKTX = ArrayList<Room>()
    private var roomListPT = ArrayList<Room>()
    private val roomsAdapter= RoomsAdapter()
    private val roomsAdapterKTX= RoomsAdapter3()
    private val roomsAdapterPT= RoomsAdapter3()
    private var dialog: AlertDialog? = null

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("room")
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListKtxRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        retrieveDataFromDatabase()


        loadDone={
            roomsAdapter.addList(roomList)
            roomsAdapterPT.addList(roomListPT)
            roomsAdapterKTX.addList(roomListKTX)
        }

        binding.rcvRoomKTX.adapter = roomsAdapterKTX

        roomsAdapterKTX.onclickItem = {
            val intent =
                Intent(this, DetailRoomActivity::class.java)
            intent.putExtra("roomId", it)
            startActivity(intent)

        }

    }
    private fun retrieveDataFromDatabase() {
        myReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                getRoomList.clear()
                roomList.clear()
                roomListKTX.clear()
                roomListPT.clear()
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
                    getRoomList.add(data)
                }
                getRoomList.forEach {
                    if(it.trang_thai_duyet == true && it.trang_thai_bai_dang == true ){
                        roomList.add(it)
                    }
                    if (it.trang_thai_duyet == true && it.trang_thai_bai_dang == true && it.id_loai_bai_dang == "Phòng trọ và nhà trọ"){
                        roomListPT.add(it)
                    }
                    if (it.trang_thai_duyet == true && it.trang_thai_bai_dang == true && (it.id_loai_bai_dang == "Kí túc xá" || it.id_loai_bai_dang == "Tìm bạn ở ghép")){
                        roomListKTX.add(it)
                    }
                }
                loadDone?.invoke()

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}