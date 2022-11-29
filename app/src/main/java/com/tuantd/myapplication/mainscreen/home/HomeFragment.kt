package com.tuantd.myapplication.mainscreen.home

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tuantd.myapplication.R
import com.tuantd.myapplication.databinding.FragmentHomeBinding
import com.tuantd.myapplication.databinding.FragmentMyListRoomHideBinding
import com.tuantd.myapplication.dialog.LoadingDialog
import com.tuantd.myapplication.mainscreen.MainActivity
import com.tuantd.myapplication.mainscreen.home.AddRoom.AddRoomActivity
import com.tuantd.myapplication.mainscreen.home.DetailRoom.DetailRoomActivity

class HomeFragment : Fragment() {
    //test
    private var loadDone:(() -> Unit)?=null
    private var getRoomList = ArrayList<Room>()
    private var roomList = ArrayList<Room>()
    private var  roomListKTX = ArrayList<Room>()
    private var roomListPT = ArrayList<Room>()
    private val roomsAdapter= RoomsAdapter()
    private val roomsAdapterKTX= RoomsAdapter2()
    private val roomsAdapterPT= RoomsAdapter2()
    private var dialog: androidx.appcompat.app.AlertDialog? = null

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("room")
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    lateinit var binding : FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createDialog()
        binding.btnAdd.setOnClickListener {
            if (auth.currentUser != null) {
                val intent = Intent(activity as MainActivity, AddRoomActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(requireContext(),"Bạn chưa đăng nhập. Hãy đăng nhập để sử dụng tính năng này",Toast.LENGTH_SHORT).show()
            }

        }
        retrieveDataFromDatabase()

        loadDone={
            roomsAdapter.addList(roomList)
            roomsAdapterPT.addList(roomListPT)
            roomsAdapterKTX.addList(roomListKTX)
        }

        binding.rcvRoom.adapter = roomsAdapter
        binding.rcvRoomKTX.adapter = roomsAdapterKTX
        binding.rcvRoomPT.adapter = roomsAdapterPT

        roomsAdapter.onclickItem = {
            val intent =
                Intent((activity as MainActivity), DetailRoomActivity::class.java)
            intent.putExtra("roomId", it)
            (activity as MainActivity).startActivity(intent)

        }
        roomsAdapterKTX.onclickItem = {
            val intent =
                Intent((activity as MainActivity), DetailRoomActivity::class.java)
            intent.putExtra("roomId", it)
            (activity as MainActivity).startActivity(intent)

        }
        roomsAdapterPT.onclickItem = {
            val intent =
                Intent((activity as MainActivity), DetailRoomActivity::class.java)
            intent.putExtra("roomId", it)
            (activity as MainActivity).startActivity(intent)

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
    private fun createDialog() {
        val dialogBuilder = AlertDialog.Builder(requireActivity())
        val dialogView = LayoutInflater.from(requireActivity()).inflate(R.layout.layout_loading, null)
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



}



