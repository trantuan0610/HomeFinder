package com.tuantd.myapplication.mainscreen.home.DetailRoom

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.support.annotation.NonNull
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tuantd.myapplication.ImageAdapter
import com.tuantd.myapplication.databinding.ActivityDetailRoomBinding
import com.tuantd.myapplication.dialog.DialogRate
import com.tuantd.myapplication.dialog.DialogSelectContact
import com.tuantd.myapplication.mainscreen.home.Rate.Rate
import com.tuantd.myapplication.mainscreen.home.Report.ReportActivity
import com.tuantd.myapplication.mainscreen.home.Room
import com.tuantd.myapplication.mainscreen.home.RoomsAdapter
import com.tuantd.myapplication.register.User
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*


class DetailRoomActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailRoomBinding
    private val roomsAdapter= RoomsAdapter()
    private var roomList = ArrayList<Room>()
    private var roomLienquan = ArrayList<Room>()
    private var roomFavList = ArrayList<FavouriteRoom>()
    private var likeList = ArrayList<FavouriteRoom>()
    val rateList = ArrayList<Rate>()
    private var roomDetail: Room? = null

    private val imageAdapter = ImageAdapter()
    private var sodiem = 0.0
    private var dem = 0
    var rated = 0

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("room")
    private val myFavouriteReferene: DatabaseReference = database.reference.child("favourite")
    private val myRateReferene: DatabaseReference = database.reference.child("rate")
    private val myUserReference: DatabaseReference = database.reference.child("user")
    private var userDetail: User? = null
    val userList = ArrayList<User>()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //test trên master branch
        binding.back.setOnClickListener {
            onBackPressed()
        }
        getUser()
        val roomId = intent.getStringExtra("roomId")
        if (roomId != null) {
            retrieveDataFromDatabase(roomId)
            if(auth.currentUser?.email !=null){
                getFavouriteRoomLike()
            }

            getListRate()
        }


        binding.like.setOnClickListener {
            actionLike()
        }

        binding.dontlike.setOnClickListener {
            actionDontLike()
        }

        binding.share.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, "Địa chỉ:" + roomDetail?.dia_chi +"\n"+"Anh/chị:"+roomDetail?.name+"\n"+"SDT:"+roomDetail?.sdt)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Please select app: "))
        }

        roomsAdapter.onclickItem = {
            val intent =
                Intent(this, DetailRoomActivity::class.java)
            intent.putExtra("roomId", it)
            startActivity(intent)
        }
        //lien lac tin nhan, dien thoai
        binding.btnMore.setOnClickListener {
            DialogSelectContact(onSubmitClickListener = { it ->

                when(it){
                    1 -> { val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:${roomDetail?.sdt}")
                        startActivity(intent)
                    }

                    2-> {
                        var n = Intent(Intent.ACTION_VIEW,Uri.parse("smsto:"+roomDetail?.sdt))
                        n.putExtra("sms_body","Xin chào, Tôi muốn thuê phòng trọ ở "+"Địa chỉ: " + roomDetail?.dia_chi +" của bạn!!!")
                        startActivity(n)
//                    val n = Intent(Intent.ACTION_VIEW)
//                    n.type = "vnd.android-dir/mms-sms"
//                    n.putExtra("address", roomDetail?.sdt)
//                    n.putExtra("sms_body", "Xin chào, Tôi muốn thuê phòng trọ của bạn!!!")
//                    startActivity(n)
                    }
                    3-> {
                        val map = "http://maps.google.co.in/maps?q=${roomDetail?.dia_chi}"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(map))
                            startActivity(intent)
                    }
                    4-> {
                        if (auth.currentUser != null) {
                            if(!(rated > 0)){
                                DialogRate(roomDetail?.id_bai_dang, onSubmitClickListener = {
                                    if (it==1){
                                        Toast.makeText(this,"Đánh giá sao thành công",Toast.LENGTH_SHORT).show()
                                    }
                                }).show(supportFragmentManager,"tag1")
                            }else{
                                Toast.makeText(this,"Bạn đã đánh giá sao cho bài đăng này",Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(this,"Bạn chưa đăng nhập. Hãy đăng nhập để sử dụng tính năng này",Toast.LENGTH_SHORT).show()
                        }


                    }
                    5-> {
                        if (auth.currentUser != null) {
                            val intent = Intent(this, ReportActivity::class.java)
                            intent.putExtra("roomId", roomDetail?.id_bai_dang)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this,"Bạn chưa đăng nhập. Hãy đăng nhập để sử dụng tính năng này",Toast.LENGTH_SHORT).show()
                        }


                    }
                }

            }).show(supportFragmentManager,"tag")
        }
    }
    private fun setTextMoney(text: String) {
        val symbols = DecimalFormatSymbols()
        symbols.setDecimalSeparator(',')
        val decimalFormat = DecimalFormat("###,###,###,###Đ", symbols)
        binding.tvPrice.setText(decimalFormat.format(text.toInt()))
    }
    private fun getUser() {
        myUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (user in snapshot.children) {
                    val uSer = user.value as? HashMap<*, *>
                    val data = com.tuantd.myapplication.register.User(
                        id_nguoi_dung =  uSer?.get("id_nguoi_dung") as String,
                        email = uSer["email"] as String,
                        mat_khau = uSer["mat_khau"] as String,
                        quyen = uSer["quyen"] as String,
                        sdt = uSer["sdt"] as String,
                        ten = uSer["ten"] as String
                    )
                    userList.add(data)
                }
                userList.forEach {
                    if (it.email == auth.currentUser?.email  ) {
                        userDetail = it
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private  fun  getListRate(){
        myRateReferene.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                rateList.clear()
                for (rate in snapshot.children) {
                    val mRate = rate.value as? HashMap<*, *>
                    val data = Rate(
                        id_rate = mRate?.get("id_rate") as String,
                        id_bai_dang = mRate["id_bai_dang"] as String,
                        id_nguoi_dung = mRate["id_nguoi_dung"] as String,
                        so_diem = mRate["so_diem"] as String
                    )
                    rateList.add(data)
                }
                rateList.forEach{
                    if(it.id_bai_dang == roomDetail?.id_bai_dang && it.id_nguoi_dung == userDetail?.id_nguoi_dung){
                        rated = 1
                    }
                }
                rateList.forEach {
                    if(it.id_bai_dang == roomDetail?.id_bai_dang){
                        sodiem = sodiem + it.so_diem.toDouble()
                        dem = dem + 1
                    }
                }
                if(sodiem>0){
                    binding.tvRate.text = "Đánh giá: "+(sodiem/dem).toString().substring(0,3)+"/5"
                }else{
                    binding.tvRate.text = "Chưa có đánh giá!"
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    // get data ve va luu data vao roomlist
    private fun retrieveDataFromDatabase(roomID: String) {
        myReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                roomList.clear()
                roomLienquan.clear()
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
                roomList.forEach {
                    if (it.id_bai_dang.equals(roomID)) {
                        roomDetail = it
                        binding.tvRoomAddress.text = it.dia_chi
                        //binding.tvPrice.text = it.gia + "Đ"
                        setTextMoney(it.gia)
                        binding.tvRoomArea.text = it.dien_tich + "m2"
                        binding.tvDetailDes.text = it.mo_ta
                        binding.tvtitle.text = it.tieu_de
                        binding.tvTitle2.text = it.tieu_de
                        binding.tvName.text = it.name + " - " + it.sdt
                        binding.tvDate.text = "Ngày đăng: " + it.thoi_gian
                        binding.tvLoaiPhong.text = "Loại Phòng: " + it.id_loai_bai_dang

                        imageAdapter.addList(it?.list_image as ArrayList<String>)
                        binding.rcvImage.adapter = imageAdapter

                        if (it.wifi == true) {
                            binding.ltWifiOn.visibility = View.VISIBLE
                            binding.ltWifiOff.visibility = View.GONE
                        } else {
                            binding.ltWifiOff.visibility = View.VISIBLE
                            binding.ltWifiOn.visibility = View.GONE

                        }
                        if (it.nha_ve_sinh == true) {
                            binding.ltWcOn.visibility = View.VISIBLE
                            binding.ltWcOff.visibility = View.GONE
                        } else {
                            binding.ltWcOn.visibility = View.GONE
                            binding.ltWcOff.visibility = View.VISIBLE

                        }
                        if (it.bep_nau == true) {
                            //
                            binding.ltKitchenOn.visibility = View.VISIBLE
                            binding.ltKichenOff.visibility = View.GONE
                        } else {
                            binding.ltKichenOff.visibility = View.VISIBLE
                            binding.ltKitchenOn.visibility = View.GONE

                        }
                        if (it.giu_xe == true) {
                            binding.ltParkingOn.visibility = View.VISIBLE
                            binding.ltParkingOff.visibility = View.GONE
                        } else {
                            binding.ltParkingOff.visibility = View.VISIBLE
                            binding.ltParkingOn.visibility = View.GONE

                        }
                        if (it.dieu_hoa == true) {
                            binding.ltAirConditionalOn.visibility = View.VISIBLE
                            binding.ltAirConditionalOff.visibility = View.GONE
                        } else {
                            binding.ltAirConditionalOff.visibility = View.VISIBLE
                            binding.ltAirConditionalOn.visibility = View.GONE

                        }
                        if (it.tu_lanh == true) {
                            binding.ltFridgeOn.visibility = View.VISIBLE
                            binding.ltFridgeOff.visibility = View.GONE
                        } else {
                            binding.ltFridgeOff.visibility = View.VISIBLE
                            binding.ltFridgeOn.visibility = View.GONE

                        }
                        if (it.tu_do == true) {
                            binding.ltFreeOn.visibility = View.VISIBLE
                            binding.ltFreeOff.visibility = View.GONE
                        } else {
                            binding.ltFreeOff.visibility = View.VISIBLE
                            binding.ltFreeOn.visibility = View.GONE

                        }
                        if (it.may_giat == true) {
                            binding.ltWashingMachineOn.visibility = View.VISIBLE
                            binding.ltWashingMachineOff.visibility = View.GONE
                        } else {
                            binding.ltWashingMachineOff.visibility = View.VISIBLE
                            binding.ltWashingMachineOn.visibility = View.GONE

                        }

                    }

                }
                roomList.forEach {
                    if (it.trang_thai_duyet == true && it.trang_thai_bai_dang == true){
                        roomLienquan.add(it)
                    }
                }
                roomsAdapter.addList(roomLienquan)
                binding.rcvTinlienquan.adapter = roomsAdapter

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun getFavouriteRoomLike() {
        myFavouriteReferene.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                roomFavList.clear()
                likeList.clear()
                for (favRoom in snapshot.children) {
                    val favRoom = favRoom.value as? HashMap<*, *>
                    val data = FavouriteRoom(
                        id_yeu_thich = favRoom?.get("id_yeu_thich") as String,
                        id_bai_dang = favRoom["id_bai_dang"] as String,
                        id_nguoi_dung = favRoom["id_nguoi_dung"] as String
                    )
                    roomFavList.add(data)
                }
                Log.e("AAAAA", "room fav list" + roomFavList.size.toString())
                roomFavList.forEach {
                    if (it.id_nguoi_dung == userDetail!!.id_nguoi_dung && it.id_bai_dang == roomDetail?.id_bai_dang ) {
                        setLike_DontLike(it)
                    }
                }
                Log.e("AAAAA", "Like list" + likeList.size.toString())

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun setLike_DontLike(favouriteRoom: FavouriteRoom) {
            if (favouriteRoom!= null){
                binding.dontlike.visibility = View.VISIBLE
                binding.like.visibility =View.GONE
            }else{
                binding.dontlike.visibility = View.GONE
                binding.like.visibility = View.VISIBLE
            }
    }

    // like room (done)
    private fun actionLike() {
        if (auth.currentUser?.email != null){
            binding.like.visibility = View.GONE
            binding.dontlike.visibility = View.VISIBLE
            val id = myFavouriteReferene.push().key.toString()
            val favouriteRoom = FavouriteRoom(
                id,
                roomDetail!!.id_bai_dang,
                userDetail!!.id_nguoi_dung

            )
            myFavouriteReferene.child(id).setValue(favouriteRoom).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Lưu thành công", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(this,"Bạn phải đăng nhập để lưu phòng",Toast.LENGTH_SHORT).show()
        }


    }

    //done
    private fun actionDontLike() {
        if(auth.currentUser?.email != null){
            roomFavList.forEach {
                if (it.id_bai_dang == roomDetail?.id_bai_dang && it.id_nguoi_dung == userDetail!!.id_nguoi_dung ) {
                    binding.like.visibility = View.VISIBLE
                    binding.dontlike.visibility = View.GONE
                    myFavouriteReferene.child(it.id_yeu_thich).removeValue()
                    Toast.makeText(this, "Bỏ lưu thành công", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(this,"Bạn phải đăng nhập để lưu phòng",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_OK,Intent())
    }
}

