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
import com.tuantd.myapplication.mainscreen.MainActivity
import com.tuantd.myapplication.mainscreen.home.Rate.Rate
import com.tuantd.myapplication.mainscreen.home.Report.ReportActivity
import com.tuantd.myapplication.mainscreen.home.Room
import com.tuantd.myapplication.mainscreen.home.RoomsAdapter
import java.math.RoundingMode
import java.text.DecimalFormat


class DetailRoomActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailRoomBinding
    private val roomsAdapter= RoomsAdapter()
    private var roomList = ArrayList<Room>()
    private var roomFavList = ArrayList<FavouriteRoom>()
    private var likeList = ArrayList<FavouriteRoom>()
    val rateList = ArrayList<Rate>()
    private var roomDetail: Room? = null
    private val imageAdapter = ImageAdapter()
    private var sodiem = 0.0
    private var dem = 0

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("room")
    private val myFavouriteReferene: DatabaseReference = database.reference.child("favourite")
    private val myRateReferene: DatabaseReference = database.reference.child("rate")
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            onBackPressed()
        }

        val roomId = intent.getStringExtra("roomId")
        if (roomId != null) {
            retrieveDataFromDatabase(roomId)
            getFavouriteRoomLike()
            getListRate()
        }


        binding.like.setOnClickListener {
            actionLike()
        }

        binding.dontlike.setOnClickListener {
            actionDontLike()
        }
        roomsAdapter.addList(roomList)
        binding.rcvTinlienquan.adapter = roomsAdapter
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
                        val n = Intent(Intent.ACTION_VIEW)
                    n.type = "vnd.android-dir/mms-sms"
                    n.putExtra("address", roomDetail?.sdt)
                    n.putExtra("sms_body", "Xin chào, Tôi muốn thuê phòng trọ của bạn!!!")
                    startActivity(n)
                    }
                    3-> {}
                    4-> {if(!Rated()){
                        DialogRate(roomDetail?.id_bai_dang, onSubmitClickListener = {
                            if (it==1){
                                Toast.makeText(this,"Đánh giá sao thành công",Toast.LENGTH_SHORT).show()
                            }
                        }).show(supportFragmentManager,"tag1")
                    }else{
                        Toast.makeText(this,"Bạn đã đánh giá sao cho bài đăng này",Toast.LENGTH_SHORT).show()
                    }

                    }
                    5-> {
                        val intent = Intent(this, ReportActivity::class.java)
                        intent.putExtra("roomId", roomDetail?.id_bai_dang)
                        startActivity(intent)
                    }
                }

            }).show(supportFragmentManager,"tag")
        }
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
                        so_diem = mRate["so_diem"]as String
                    )
                    rateList.add(data)
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
                    binding.tvRate.text = "Chưa có Đánh giá!"
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun Rated(): Boolean {
        return rateList.size > 0

    }

    fun getDefaultSmsAppPackageName(@NonNull context: Context): String? {
        val defaultSmsPackageName: String
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context)
            return defaultSmsPackageName
        } else {
            val intent = Intent(Intent.ACTION_VIEW)
                .addCategory(Intent.CATEGORY_DEFAULT).setType("vnd.android-dir/mms-sms")
            val resolveInfos: List<ResolveInfo> =
                context.getPackageManager().queryIntentActivities(intent, 0)
            if (resolveInfos != null && !resolveInfos.isEmpty()) return resolveInfos[0].activityInfo.packageName
        }
        return null
    }

    // get data ve va luu data vao roomlist
    private fun retrieveDataFromDatabase(roomID: String) {
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
                roomList.forEach {
                    if (it.id_bai_dang.equals(roomID)) {
                        roomDetail = it
                        binding.tvRoomAddress.text = it.dia_chi
                        binding.tvPrice.text = it.gia + "Đ"
                        binding.tvRoomArea.text = it.dien_tich + "m2"
                        binding.tvDetailDes.text = it.mo_ta
                        binding.tvtitle.text = it.tieu_de
                        binding.tvName.text = it.name + "-" + it.sdt
                        binding.tvDate.text = "Ngày đăng: " + it.thoi_gian

                        imageAdapter.addList(it?.list_image as ArrayList<String>)
                        binding.rcvImage.adapter = imageAdapter

                        if (it.wifi == "1") {
                            binding.wifiOn.visibility = View.VISIBLE
                            binding.wifiOff.visibility = View.GONE
                        } else {
                            binding.wifiOff.visibility = View.VISIBLE
                            binding.wifiOn.visibility = View.GONE

                        }
                        if (it.nha_ve_sinh == "1") {
                            binding.wcOn.visibility = View.VISIBLE
                            binding.wcOff.visibility = View.GONE
                        } else {
                            binding.wcOn.visibility = View.GONE
                            binding.wcOff.visibility = View.VISIBLE

                        }
                        if (it.bep_nau == "1") {
                            binding.kitchenOn.visibility = View.VISIBLE
                            binding.kitchenOff.visibility = View.GONE
                        } else {
                            binding.kitchenOff.visibility = View.VISIBLE
                            binding.kitchenOn.visibility = View.GONE

                        }
                        if (it.giu_xe == "1") {
                            binding.parkingOn.visibility = View.VISIBLE
                            binding.parkingOff.visibility = View.GONE
                        } else {
                            binding.parkingOff.visibility = View.VISIBLE
                            binding.parkingOn.visibility = View.GONE

                        }
                        if (it.dieu_hoa == "1") {
                            binding.airConditionalOn.visibility = View.VISIBLE
                            binding.airConditionalOff.visibility = View.GONE
                        } else {
                            binding.airConditionalOff.visibility = View.VISIBLE
                            binding.airConditionalOn.visibility = View.GONE

                        }
                        if (it.tu_lanh == "1") {
                            binding.fridgeOn.visibility = View.VISIBLE
                            binding.fridgeOff.visibility = View.GONE
                        } else {
                            binding.fridgeOff.visibility = View.VISIBLE
                            binding.fridgeOn.visibility = View.GONE

                        }
                        if (it.tu_do == "1") {
                            binding.freeOn.visibility = View.VISIBLE
                            binding.freeOff.visibility = View.GONE
                        } else {
                            binding.freeOff.visibility = View.VISIBLE
                            binding.freeOn.visibility = View.GONE

                        }
                        if (it.may_giat == "1") {
                            binding.washingMachineOn.visibility = View.VISIBLE
                            binding.washingMachineOff.visibility = View.GONE
                        } else {
                            binding.washingMachineOff.visibility = View.VISIBLE
                            binding.washingMachineOn.visibility = View.GONE

                        }

                    }

                }

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
                    if (it.id_nguoi_dung == auth.currentUser?.email && it.id_bai_dang == roomDetail?.id_bai_dang ) {
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
        binding.like.visibility = View.GONE
        binding.dontlike.visibility = View.VISIBLE
        val id = myFavouriteReferene.push().key.toString()
        val favouriteRoom = FavouriteRoom(
            id,
            roomDetail!!.id_bai_dang,
            auth.currentUser?.email.toString()

        )
        myFavouriteReferene.child(id).setValue(favouriteRoom).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Lưu thành công", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Lưu thất bại", Toast.LENGTH_SHORT).show()
            }
        }

    }

    //done
    private fun actionDontLike() {
        roomFavList.forEach {
            if (it.id_bai_dang == roomDetail?.id_bai_dang && it.id_nguoi_dung == auth.currentUser?.email.toString() ) {
                binding.like.visibility = View.VISIBLE
                binding.dontlike.visibility = View.GONE
                myFavouriteReferene.child(it.id_yeu_thich).removeValue()
                Toast.makeText(this, "Bỏ lưu thành công", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Bỏ lưu thất bại", Toast.LENGTH_SHORT).show()
            }

        }

    }
}

