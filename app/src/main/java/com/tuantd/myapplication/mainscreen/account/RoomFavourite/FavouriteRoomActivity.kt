package com.tuantd.myapplication.mainscreen.account.RoomFavourite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tuantd.myapplication.R
import com.tuantd.myapplication.databinding.ActivityFavouriteRoomBinding
import com.tuantd.myapplication.mainscreen.home.DetailRoom.DetailRoomActivity
import com.tuantd.myapplication.mainscreen.home.DetailRoom.FavouriteRoom
import com.tuantd.myapplication.mainscreen.home.Room
import com.tuantd.myapplication.mainscreen.home.RoomsAdapter

class FavouriteRoomActivity : AppCompatActivity() {

    private var loadDone:(() -> Unit)?=null
    private var getfavroomList = ArrayList<FavouriteRoom>()
    private var favroomList = ArrayList<FavouriteRoom>()
    private var getRoomList = ArrayList<Room>()
    private var roomList = ArrayList<Room>()
     var roomsFavAdapter= RoomsAdapter()
    lateinit var binding : ActivityFavouriteRoomBinding
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("favourite")
    private val roomReference: DatabaseReference = database.reference.child("room")
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)


        retrieveFavRoomFromDatabase()

        loadDone={
            roomsFavAdapter.addList(roomList)
        }

        binding.rcvFavRoom.adapter = roomsFavAdapter

        roomsFavAdapter.onclickItem = {
            val intent =
                Intent(this, DetailRoomActivity::class.java)
            intent.putExtra("roomId", it)
            startActivity(intent)
            finish()
        }
    }
    private fun retrieveFavRoomFromDatabase() {
        myReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                getfavroomList.clear()
                favroomList.clear()
                roomList.clear()
                for (favRoom in snapshot.children) {
                    val favRoom = favRoom.value as? HashMap<*, *>
                    val data = FavouriteRoom(
                        id_yeu_thich = favRoom?.get("id_yeu_thich") as String,
                        id_nguoi_dung = favRoom["id_nguoi_dung"] as String,
                        id_bai_dang = favRoom["id_bai_dang"] as String
                    )
                    getfavroomList.add(data)
                }
                getfavroomList.forEach {
                    if (it.id_nguoi_dung == auth.currentUser?.email) {
                        favroomList.add(it)
                    }
                }
                favroomList.forEach {
                    retrieveRoomFromDatabase(it.id_bai_dang)
                }

            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    private fun retrieveRoomFromDatabase(roomId: String){
        roomReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                getRoomList.clear()
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
                    if(it.trang_thai_duyet == true && it.trang_thai_bai_dang ==true && it.id_bai_dang == roomId){
                        roomList.add(it)
                    }
                }
                loadDone?.invoke()

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }



}