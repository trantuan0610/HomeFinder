package com.tuantd.myapplication.mainscreen.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.tuantd.myapplication.R
import com.tuantd.myapplication.mainscreen.MainActivity
import com.tuantd.myapplication.mainscreen.home.DetailRoom.DetailRoomActivity
import com.tuantd.myapplication.mainscreen.home.Room
import com.tuantd.myapplication.mainscreen.home.RoomsAdapter


class SearchFragment : Fragment() {
    private var roomList = ArrayList<Room>()
    private var arrayList = ArrayList<Room>()
    lateinit var rcv_searchRoom: RecyclerView
    private var roomsAdapter: RoomsAdapter?=null
    lateinit var searchView: SearchView
    lateinit var tvHistory: TextView

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("room")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        rcv_searchRoom = view.findViewById(R.id.rcv_searchRooms)
        searchView = view.findViewById(R.id.searchView)
        tvHistory = view.findViewById(R.id.tvHiss)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retrieveDataFromDatabase()
        roomsAdapter = RoomsAdapter()
        rcv_searchRoom.adapter = roomsAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                    tvHistory.visibility =View.GONE
                    arrayList.clear()
                if (newText!!.isNotEmpty()) {
                    val search = newText.toString().lowercase()
                    roomList.forEach {
                        if (it.dia_chi?.lowercase()?.contains(search) == true) {
                            arrayList.add(it)
                        }
                    }
                    roomsAdapter?.addList(arrayList)
                    roomsAdapter!!.notifyDataSetChanged()
                    roomsAdapter!!.onclickItem = {
                        val intent =
                            Intent((activity as MainActivity), DetailRoomActivity::class.java)
                        intent.putExtra("roomId", it)
                        (activity as MainActivity).startActivity(intent)
                    }
                } else {
                    arrayList.clear()
                    tvHistory.visibility = View.VISIBLE
                    roomsAdapter?.addList(arrayList)
                }

                return true
            }

        })
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

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}