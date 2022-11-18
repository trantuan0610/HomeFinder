package com.tuantd.myapplication.mainscreen.account.myRoom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tuantd.myapplication.databinding.ActivityMyRoomBinding
import com.tuantd.myapplication.mainscreen.home.DetailRoom.DetailRoomActivity
import com.tuantd.myapplication.mainscreen.home.Room
import com.tuantd.myapplication.mainscreen.home.RoomsAdapter

class MyRoomActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyRoomBinding
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("room")
    private var roomList = ArrayList<Room>()
    private var arrayList = ArrayList<Room>()
    private var roomsAdapter = RoomsAdapter()
    private var loadDone:(() -> Unit)?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retrieveDataFromDatabase(FirebaseAuth.getInstance().currentUser!!.email.toString())
        loadDone={
            roomsAdapter?.addList(arrayList)
        }
        binding.rcvRoom.adapter = roomsAdapter
        roomsAdapter.onclickItem = {
            val intent =
                Intent(this, DetailMyRoomActivity::class.java)
            intent.putExtra("roomId", it)
            startActivity(intent)
            finish()
        }
    }

    private fun retrieveDataFromDatabase(email: String) {
        myReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                roomList.clear()
                for (eachRoom in snapshot.children) {
                    val room = eachRoom.value as? HashMap<String, Any?>
                    val data =  Room(
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
                fillDataFromFireBase(email)
                loadDone?.invoke()
                //roomsAdapter?.addList(roomList)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun fillDataFromFireBase(email: String) {
        roomList.forEach {
            if (it.id_nguoi_dung == email) {
                arrayList.add(it)
            }
        }
        roomsAdapter?.addList(arrayList)

    }
}