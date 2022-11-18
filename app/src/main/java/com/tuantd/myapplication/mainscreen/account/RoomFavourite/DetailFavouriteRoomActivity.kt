package com.tuantd.myapplication.mainscreen.account.RoomFavourite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tuantd.myapplication.R
import com.tuantd.myapplication.databinding.ActivityDetailFavouriteRoomBinding
import com.tuantd.myapplication.databinding.ActivityFavouriteRoomBinding
import com.tuantd.myapplication.mainscreen.home.DetailRoom.FavouriteRoom
import com.tuantd.myapplication.mainscreen.home.Room

class DetailFavouriteRoomActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailFavouriteRoomBinding
    private var favouriteList = ArrayList<FavouriteRoom>()
    private var roomList = ArrayList<Room>()
    private var roomDetail: FavouriteRoom? = null

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myFavouriteReferene = database.reference.child("favourite")
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFavouriteRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.back.setOnClickListener {
            onBackPressed()
        }

        val roomFavId = intent.getStringExtra("roomFavId")
        if (roomFavId != null) {
            retrieveDataFromDatabase(roomFavId)
        }
        binding.like.setOnClickListener {
            actionLike()
        }

        binding.dontlike.setOnClickListener {
            actionDontLike()
        }


    }

    private fun actionDontLike() {
        favouriteList.forEach {
            if (it.id_bai_dang.equals(roomDetail?.id_bai_dang)) {
                binding.like.visibility = View.VISIBLE
                binding.dontlike.visibility = View.GONE
                myFavouriteReferene.child(it.id_yeu_thich).removeValue()
                Toast.makeText(this, "Bỏ lưu thành công", Toast.LENGTH_SHORT).show()
            } else {
                binding.like.visibility = View.GONE
                binding.dontlike.visibility = View.VISIBLE

            }

        }

    }

    private fun actionLike() {
        binding.like.visibility = View.GONE
        binding.dontlike.visibility = View.VISIBLE

        val favouriteRoom = FavouriteRoom(
            roomDetail!!.id_yeu_thich,
            auth.currentUser?.email.toString(),
            roomDetail!!.id_bai_dang,
        )
        myFavouriteReferene.child(roomDetail!!.id_yeu_thich).setValue(favouriteRoom).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Xong", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Cuts", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun retrieveDataFromDatabase(roomFavId: String) {
        myFavouriteReferene.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                favouriteList.clear()
                for (eachRoom in snapshot.children) {
                    val favRoom = eachRoom.value as? HashMap<*, *>
                    val data = FavouriteRoom(
                        id_yeu_thich = favRoom?.get("id_yeu_thich") as String,
                        id_bai_dang = favRoom["id_bai_dang"] as String,
                        id_nguoi_dung = favRoom["id_nguoi_dung"] as String
                    )
                    favouriteList.add(data)
                }
                // chưa lọc lấy id room

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    // lấy data từ room về và so sánh vs id room trên
//    roomList.forEach {
//        if (it.favouriteRoomID.equals(roomFavId)) {
//            roomDetail = it
//            binding.tvRoomAddress.text = it.roomAddressFav
//            binding.tvPrice.text = it.priceFav + "triệu"
//            binding.tvRoomArea.text = it.roomAreaFav + "m2"
//            binding.tvdes.text = it.roomDescriptionFav
//            binding.tvName.text = it.nameFav + "-" + it.phoneFav
//            Glide.with(applicationContext).load(it.roomImageFav).into(binding.imgRoom)
//
//            if (it.wifiFav == "1") {
//                binding.wifiOn.visibility = View.VISIBLE
//                binding.wifiOff.visibility = View.GONE
//            } else {
//                binding.wifiOff.visibility = View.VISIBLE
//                binding.wifiOn.visibility = View.GONE
//
//            }
//            if (it.wcFav == "1") {
//                binding.wcOn.visibility = View.VISIBLE
//                binding.wcOff.visibility = View.GONE
//            } else {
//                binding.wcOn.visibility = View.GONE
//                binding.wcOff.visibility = View.VISIBLE
//
//            }
//            if (it.kitchenFav == "1") {
//                binding.kitchenOn.visibility = View.VISIBLE
//                binding.kitchenOff.visibility = View.GONE
//            } else {
//                binding.kitchenOff.visibility = View.VISIBLE
//                binding.kitchenOn.visibility = View.GONE
//
//            }
//            if (it.parkingFav == "1") {
//                binding.parkingOn.visibility = View.VISIBLE
//                binding.parkingOff.visibility = View.GONE
//            } else {
//                binding.parkingOff.visibility = View.VISIBLE
//                binding.parkingOn.visibility = View.GONE
//
//            }
//            if (it.airConditionalFav == "1") {
//                binding.airConditionalOn.visibility = View.VISIBLE
//                binding.airConditionalOff.visibility = View.GONE
//            } else {
//                binding.airConditionalOff.visibility = View.VISIBLE
//                binding.airConditionalOn.visibility = View.GONE
//
//            }
//            if (it.fridgeFav == "1") {
//                binding.fridgeOn.visibility = View.VISIBLE
//                binding.fridgeOff.visibility = View.GONE
//            } else {
//                binding.fridgeOff.visibility = View.VISIBLE
//                binding.fridgeOn.visibility = View.GONE
//
//            }
//            if (it.freeFav == "1") {
//                binding.freeOn.visibility = View.VISIBLE
//                binding.freeOff.visibility = View.GONE
//            } else {
//                binding.freeOff.visibility = View.VISIBLE
//                binding.freeOn.visibility = View.GONE
//
//            }
//            if (it.washingMachineFav == "1") {
//                binding.washingMachineOn.visibility = View.VISIBLE
//                binding.washingMachineOff.visibility = View.GONE
//            } else {
//                binding.washingMachineOff.visibility = View.VISIBLE
//                binding.washingMachineOn.visibility = View.GONE
//
//            }
//
//        }
//
//    }
}