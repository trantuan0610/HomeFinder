package com.tuantd.myapplication.mainscreen.search

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.NumberFormat
import android.icu.util.Currency
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.android.material.slider.RangeSlider
import com.google.firebase.database.*
import com.tuantd.myapplication.R
import com.tuantd.myapplication.databinding.FragmentSearchBinding
import com.tuantd.myapplication.mainscreen.MainActivity
import com.tuantd.myapplication.mainscreen.home.DetailRoom.DetailRoomActivity
import com.tuantd.myapplication.mainscreen.home.Room
import com.tuantd.myapplication.mainscreen.home.RoomsAdapter3


class SearchFragment : Fragment() {
    private var roomList = ArrayList<Room>()
    private var roomList2 = ArrayList<Room>()
    private var arrayList = ArrayList<Room>()
    private var roomsAdapter: RoomsAdapter3? = null
    private var roomsAdsAdapter: RoomsAdapter3? = null
    lateinit var binding: FragmentSearchBinding

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("room")

    lateinit var adapterItemText: ArrayAdapter<String>
    var itemtext = arrayOf("Kí túc xá", "Phòng trọ và nhà trọ", "Nhà Nguyên Căn", "Tìm bạn ở ghép")
    var item = " "


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //test souce tree
        adapterItemText = ArrayAdapter(requireContext(), R.layout.item_text, itemtext)
        binding.autoTxt.setAdapter(adapterItemText)

        retrieveDataFromDatabase()
        roomsAdapter = RoomsAdapter3()
        roomsAdsAdapter = RoomsAdapter3()
        binding.rcvSearchRooms.adapter = roomsAdapter
        binding.rcvSearchAds.adapter = roomsAdsAdapter

        binding.btnSearchAdv.setOnClickListener {
            binding.man1.visibility = View.GONE
            binding.man2.visibility = View.VISIBLE
            binding.btnSearchAdv.visibility = View.GONE
            binding.tvTitle.setText("Tìm Kiếm Nâng Cao")
        }

        binding.btnHuy.setOnClickListener {
            binding.man2.visibility = View.GONE
            binding.man1.visibility = View.VISIBLE
            binding.btnSearchAdv.visibility = View.VISIBLE
            binding.tvTitle.setText("Tìm Kiếm")
        }
        //Area


        binding.sliderRangeArea.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener{
            @SuppressLint("RestrictedApi")
            override fun onStartTrackingTouch(slider: RangeSlider) {
                val values = binding.sliderRangeArea.values

            }

            @SuppressLint("RestrictedApi")
            override fun onStopTrackingTouch(slider: RangeSlider) {
                val values = binding.sliderRangeArea.values
                //Those are the new updated values of sldier when user has finshed dragging
                binding.edtAreaMin.setText(values[0].toInt().toString())
                binding.edtAreaMax.setText(values[1].toInt().toString())
            }
        })


        binding.sliderRangeArea.addOnChangeListener { slider, value, fromUser ->
            val values = binding.sliderRangeArea.values
            binding.edtAreaMin.setText(values[0].toInt().toString())
            binding.edtAreaMax.setText(values[1].toInt().toString())
            if(binding.edtAreaMax.text.toString() == "100"){
                binding.tvPlus2.visibility = View.VISIBLE
            }else{
                binding.tvPlus2.visibility = View.GONE
            }
        }
        //Price
        binding.sliderRangePrice.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener{
            @SuppressLint("RestrictedApi")
            override fun onStartTrackingTouch(slider: RangeSlider) {
                val values = binding.sliderRangePrice.values

            }

            @SuppressLint("RestrictedApi")
            override fun onStopTrackingTouch(slider: RangeSlider) {
                val values = binding.sliderRangePrice.values
                //Those are the new updated values of sldier when user has finshed dragging
                binding.edtPriceMin.setText(values[0].toInt().toString())
                binding.edtpriceMax.setText(values[1].toInt().toString())
            }
        })


        binding.sliderRangePrice.addOnChangeListener { slider, value, fromUser ->
            val values = binding.sliderRangePrice.values
            binding.edtPriceMin.setText(values[0].toInt().toString())
            binding.edtpriceMax.setText(values[1].toInt().toString())
            if(binding.edtpriceMax.text.toString() == "20"){
                binding.tvPlus.visibility = View.VISIBLE
            }else{
                binding.tvPlus.visibility = View.GONE
            }
        }
        //



        binding.btnOk.setOnClickListener {
            if (binding.edtSearch.text.toString().isNotEmpty()
                && binding.autoTxt.text.toString().isNotEmpty()
                && binding.edtPriceMin.text.toString().isNotEmpty()
                && binding.edtpriceMax.text.toString().isNotEmpty()
                && binding.edtAreaMin.text.toString().isNotEmpty()
                && binding.edtAreaMax.text.toString().isNotEmpty()
            ) {
                if (binding.edtpriceMax.text.toString() == "20" || binding.edtAreaMax.text.toString() == "100"){
                    if(binding.edtAreaMax.text.toString() == "100" && binding.edtpriceMax.text.toString() == "20" ){
                        roomList2.clear()
                        roomList.forEach {
                            val diachi = binding.edtSearch.text.toString()
                            val loaiphong = binding.autoTxt.text.toString()
                            val giaMin = binding.edtPriceMin.text.toString().toDouble()
                            val giaMax = binding.edtpriceMax.text.toString().toDouble()
                            val dientichMin = binding.edtAreaMin.text.toString().toDouble()
                            val dientichMax = binding.edtAreaMax.text.toString().toDouble()
                            if (it.dia_chi!!.lowercase()
                                    .contains(diachi) && it.id_loai_bai_dang == loaiphong && it.dien_tich.toDouble() > dientichMin && it.gia.toDouble()  > (giaMin * 1000000)
                            ) {
                                roomList2.add(it)
                            }
                        }
                    }else if(binding.edtAreaMax.text.toString() == "100"){
                        roomList2.clear()
                        roomList.forEach {
                            val diachi = binding.edtSearch.text.toString()
                            val loaiphong = binding.autoTxt.text.toString()
                            val giaMin = binding.edtPriceMin.text.toString().toDouble()
                            val giaMax = binding.edtpriceMax.text.toString().toDouble()
                            val dientichMin = binding.edtAreaMin.text.toString().toDouble()
                            val dientichMax = binding.edtAreaMax.text.toString().toDouble()
                            if (it.dia_chi!!.lowercase().trim()
                                    .contains(diachi.lowercase().trim()) && it.id_loai_bai_dang == loaiphong && it.dien_tich.toDouble() > dientichMin && it.gia.toDouble()  > (giaMin * 1000000) && it.gia.toDouble() < (giaMax*1000000)
                            ) {
                                roomList2.add(it)
                            }
                        }
                    } else if( binding.edtpriceMax.text.toString() == "20"){
                        roomList2.clear()
                        roomList.forEach {
                            val diachi = binding.edtSearch.text.toString()
                            val loaiphong = binding.autoTxt.text.toString()
                            val giaMin = binding.edtPriceMin.text.toString().toDouble()
                            val giaMax = binding.edtpriceMax.text.toString().toDouble()
                            val dientichMin = binding.edtAreaMin.text.toString().toDouble()
                            val dientichMax = binding.edtAreaMax.text.toString().toDouble()
                            if (it.dia_chi!!.lowercase().trim().contains(diachi.lowercase().trim()) && it.id_loai_bai_dang == loaiphong && it.dien_tich.toDouble() > dientichMin &&
                                it.dien_tich.toDouble() < dientichMax && it.gia.toDouble()  > (giaMin * 1000000)) {
                                roomList2.add(it)
                            }
                        }
                    }
                }else{
                    roomList2.clear()
                    roomList.forEach {
                        val diachi = binding.edtSearch.text.toString()
                        val loaiphong = binding.autoTxt.text.toString()
                        val giaMin = binding.edtPriceMin.text.toString().toDouble()
                        val giaMax = binding.edtpriceMax.text.toString().toDouble()
                        val dientichMin = binding.edtAreaMin.text.toString().toDouble()
                        val dientichMax = binding.edtAreaMax.text.toString().toDouble()
                        if (it.dia_chi!!.lowercase().trim().contains(diachi.lowercase().trim())
                            && it.id_loai_bai_dang == loaiphong
                            && it.dien_tich.toDouble() > dientichMin
                            && it.dien_tich.toDouble() < dientichMax
                            && it.gia.toDouble()  > (giaMin * 1000000)
                            && it.gia.toDouble() < (giaMax*1000000)
                        ) {
                            roomList2.add(it)
                        }
                    }
                }

                if (roomList2.isNotEmpty()) {
                    binding.man2.visibility = View.GONE
                    binding.man3.visibility = View.VISIBLE
                    binding.tvBack.visibility = View.VISIBLE
                    binding.tvTitle.setText("Danh Sách Tìm Kiếm")
                    roomsAdsAdapter?.addList(roomList2)
                    roomsAdsAdapter!!.onclickItem = {
                        val intent =
                            Intent((activity as MainActivity), DetailRoomActivity::class.java)
                        intent.putExtra("roomId", it)
                        (activity as MainActivity).startActivity(intent)
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Không tìm thấy phòng phù hợp",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {
                Toast.makeText(requireContext(), "Bạn phải nhập đầy đủ dữ liệu", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.tvBack.setOnClickListener {
            binding.man3.visibility = View.GONE
            binding.man2.visibility = View.VISIBLE
            binding.tvBack.visibility = View.GONE
            binding.tvTitle.setText("Tìm Kiếm Nâng Cao")
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                binding.tvHiss.visibility = View.GONE
                arrayList.clear()
                if (newText!!.isNotEmpty()) {
                    val search = newText.toString().lowercase().trim()
                    roomList.forEach {
                        if (it.dia_chi?.lowercase()?.trim()?.contains(search) == true) {
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