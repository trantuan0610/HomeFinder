package com.tuantd.myapplication.mainscreen.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.tuantd.myapplication.R
import com.tuantd.myapplication.mainscreen.MainActivity
import com.tuantd.myapplication.mainscreen.home.AddRoom.AddRoomActivity
import com.tuantd.myapplication.mainscreen.home.DetailRoom.DetailRoomActivity

class HomeFragment : Fragment() {
    private var loadDone:(() -> Unit)?=null
    private var roomList = ArrayList<Room>()
    lateinit var rcv_room: RecyclerView
    private val roomsAdapter= RoomsAdapter()
    lateinit var btnAdd: FloatingActionButton

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("room")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rcv_room = view.findViewById(R.id.rcv_room)
        btnAdd = view.findViewById(R.id.btn_Add)

        btnAdd.setOnClickListener {
            val intent = Intent(activity as MainActivity, AddRoomActivity::class.java)
            startActivity(intent)
        }
        retrieveDataFromDatabase()

        loadDone={
            roomsAdapter.addList(roomList)
        }
        rcv_room.adapter = roomsAdapter

        roomsAdapter.onclickItem = {
            val intent =
                Intent((activity as MainActivity), DetailRoomActivity::class.java)
            intent.putExtra("roomId", it)
            (activity as MainActivity).startActivity(intent)
        }
    }

    private fun retrieveDataFromDatabase() {
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
                loadDone?.invoke()

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }




}



