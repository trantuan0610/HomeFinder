package com.tuantd.myapplication.mainscreen.search

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.tuantd.myapplication.R
import com.tuantd.myapplication.databinding.FragmentSearchBinding
import com.tuantd.myapplication.dialog.DialogRate
import com.tuantd.myapplication.dialog.DialogSelectContact
import com.tuantd.myapplication.dialog.LoadingDialog
import com.tuantd.myapplication.mainscreen.MainActivity
import com.tuantd.myapplication.mainscreen.home.DetailRoom.DetailRoomActivity
import com.tuantd.myapplication.mainscreen.home.Report.ReportActivity
import com.tuantd.myapplication.mainscreen.home.Room
import com.tuantd.myapplication.mainscreen.home.RoomsAdapter


class SearchFragment : Fragment() {
    private var roomList = ArrayList<Room>()
    private var roomList2 = ArrayList<Room>()
    private var arrayList = ArrayList<Room>()
    private var roomsAdapter: RoomsAdapter?=null
    private var roomsAdsAdapter: RoomsAdapter?=null
   lateinit var binding : FragmentSearchBinding

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("room")

    lateinit var adapterItemText : ArrayAdapter<String>
    var itemtext = arrayOf("Kí túc xá", "Phòng trọ và nhà trọ" , "Nhà Nguyên Căn" , "Tìm bạn ở ghép")
    var item = " "


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterItemText = ArrayAdapter(requireContext(), R.layout.item_text,itemtext)
        binding.autoTxt.setAdapter(adapterItemText)

        retrieveDataFromDatabase()
        roomsAdapter = RoomsAdapter()
        binding.rcvSearchRooms.adapter = roomsAdapter
        binding.rcvSearchAds.adapter = roomsAdsAdapter

        binding.btnSearchAdv.setOnClickListener {
            binding.man1.visibility = View.GONE
            binding.man2.visibility = View.VISIBLE
        }

        binding.btnHuy.setOnClickListener {
            binding.man2.visibility = View.GONE
            binding.man1.visibility = View.VISIBLE
        }
        binding.btnOk.setOnClickListener {


            if(binding.edtSearch.text.toString().isNotEmpty() || binding.autoTxt.text.toString().isNotEmpty () || binding.edtPriceMin.text.toString().isNotEmpty ()
                || binding.edtpriceMax.text.toString().isNotEmpty() || binding.edtAreaMin.text.toString().isNotEmpty() || binding.edtAreaMax.text.toString().isNotEmpty()){
                roomList.forEach {
                    roomList2.clear()
                    var diachi = binding.edtSearch.text.toString()
                    var loaiphong = binding.autoTxt.text.toString()
                    var giaMin =binding.edtPriceMin.text.toString().toInt()
                    var giaMax = binding.edtpriceMax.text.toString().toInt()
                    var dientichMin =binding.edtAreaMin.text.toString().toInt()
                    var dientichMax = binding.edtAreaMax.text.toString().toInt()
                    if (it.dia_chi!!.lowercase().contains(diachi) && it.id_loai_bai_dang == loaiphong && it.dien_tich.toInt() > dientichMin &&
                        it.dien_tich.toInt() < dientichMax && it.gia.toInt() > giaMin && it.gia.toInt() < giaMax){
                        roomList2.add(it)

                    }
                }
            }else{
                Toast.makeText(requireContext(),"Ban phai Nhap du giu lieu",Toast.LENGTH_SHORT).show()
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                binding.tvHiss.visibility =View.GONE
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
                    binding.tvHiss.visibility = View.VISIBLE
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