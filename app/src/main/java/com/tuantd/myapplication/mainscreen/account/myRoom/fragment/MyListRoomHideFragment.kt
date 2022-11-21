package com.tuantd.myapplication.mainscreen.account.myRoom.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tuantd.myapplication.R
import com.tuantd.myapplication.databinding.FragmentMyListRoomHideBinding
import com.tuantd.myapplication.mainscreen.account.myRoom.DetailMyRoomActivity
import com.tuantd.myapplication.mainscreen.home.Room
import com.tuantd.myapplication.mainscreen.home.RoomsAdapter


class MyListRoomHideFragment : Fragment() {
    lateinit var binding :  FragmentMyListRoomHideBinding
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("room")
    private var roomList = ArrayList<Room>()
    private var arrayList = ArrayList<Room>()
    private var roomsAdapter = RoomsAdapter()
    private var loadDone:(() -> Unit)?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyListRoomHideBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retrieveDataFromDatabase(FirebaseAuth.getInstance().currentUser!!.email.toString())
        loadDone={
            roomsAdapter?.addList(arrayList)
        }
        binding.rcvRoom.adapter = roomsAdapter
        roomsAdapter.onclickItem = {
            val intent =
                Intent(requireContext(), DetailMyRoomActivity::class.java)
            intent.putExtra("roomId", it)
            startActivity(intent)
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
            if (it.id_nguoi_dung == email&& it.trang_thai_duyet == false) {
                arrayList.add(it)
            }
        }
        roomsAdapter?.addList(arrayList)

    }
}