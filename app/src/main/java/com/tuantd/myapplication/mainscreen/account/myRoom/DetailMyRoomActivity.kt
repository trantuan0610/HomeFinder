package com.tuantd.myapplication.mainscreen.account.myRoom

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tuantd.myapplication.databinding.ActivityDetailMyRoomBinding
import com.tuantd.myapplication.dialog.DialogEditRoom
import com.tuantd.myapplication.mainscreen.account.myRoom.editMyRoom.EditMyRoomActivity
import com.tuantd.myapplication.mainscreen.home.AddRoom.AddRoomActivity
import com.tuantd.myapplication.mainscreen.home.DetailRoom.FavouriteRoom
import com.tuantd.myapplication.mainscreen.home.Room

class DetailMyRoomActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailMyRoomBinding
    private var roomList = ArrayList<Room>()
    private var roomFavList = ArrayList<FavouriteRoom>()
    private var likeList = ArrayList<FavouriteRoom>()
    private var roomDetail: Room? = null
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("Rooms")
    private val myFavouriteReferene: DatabaseReference = database.reference.child("MyFavouriteRoom")
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMyRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            onBackPressed()
        }

        val roomId = intent.getStringExtra("roomId")
        if (roomId != null) {
            retrieveDataFromDatabase(roomId)
        }
        binding.btnMore.setOnClickListener {
            DialogEditRoom(onSubmitClickListener = { it ->
                if (it == 1) {
                val intent = Intent(this, EditMyRoomActivity::class.java).apply {
                    putExtra("room",roomDetail)
                    startActivity(this)
                }
                    finish()

                } else {
                    myReference.child(roomDetail!!.roomId).removeValue()
                    val intent = Intent(this, MyRoomActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this,"Đã xoá",Toast.LENGTH_SHORT).show()
                    finish()

                }
            }).show(supportFragmentManager, "tag")
        }
    }

    private fun retrieveDataFromDatabase(roomID: String) {
        myReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                roomList.clear()
                for (eachRoom in snapshot.children) {
                    val room = eachRoom.value as? HashMap<*, *>
                    val data = Room(
                        roomId = room?.get("roomId") as String,
                        email = room["email"] as String,
                        roomAddress = room["roomAddress"] as String,
                        roomImage = room["roomImage"] as String,
                        price = room["price"] as String,
                        roomArea = room["roomArea"] as String,
                        roomDescription = room["roomDescription"] as String,
                        name = room["name"] as String,
                        phone = room["phone"] as String,
                        wifi = room["wifi"] as String,
                        wc = room["wc"] as String,
                        free = room["free"] as String,
                        fridge = room["fridge"] as String,
                        airConditional = room["airConditional"] as String,
                        washingMachine = room["washingMachine"] as String,
                        parking = room["parking"] as String,
                        kitchen = room["kitchen"] as String
                    )
                    roomList.add(data)
                }
                roomList.forEach {
                    if (it.roomId.equals(roomID)) {
                        roomDetail = it
                        binding.tvRoomAddress.text = it.roomAddress
                        binding.tvPrice.text = it.price + "triệu"
                        binding.tvRoomArea.text = it.roomArea + "m2"
                        binding.tvDetailDes.text = it.roomDescription
                        binding.tvName.text = it.name + "-" + it.phone
                        Glide.with(applicationContext).load(it.roomImage).into(binding.imgRoom)
                        if (it.wifi == "1") {
                            binding.wifiOn.visibility = View.VISIBLE
                            binding.wifiOff.visibility = View.GONE
                        } else {
                            binding.wifiOff.visibility = View.VISIBLE
                            binding.wifiOn.visibility = View.GONE

                        }
                        if (it.wc == "1") {
                            binding.wcOn.visibility = View.VISIBLE
                            binding.wcOff.visibility = View.GONE
                        } else {
                            binding.wcOn.visibility = View.GONE
                            binding.wcOff.visibility = View.VISIBLE

                        }
                        if (it.kitchen == "1") {
                            binding.kitchenOn.visibility = View.VISIBLE
                            binding.kitchenOff.visibility = View.GONE
                        } else {
                            binding.kitchenOff.visibility = View.VISIBLE
                            binding.kitchenOn.visibility = View.GONE

                        }
                        if (it.parking == "1") {
                            binding.parkingOn.visibility = View.VISIBLE
                            binding.parkingOff.visibility = View.GONE
                        } else {
                            binding.parkingOff.visibility = View.VISIBLE
                            binding.parkingOn.visibility = View.GONE

                        }
                        if (it.airConditional == "1") {
                            binding.airConditionalOn.visibility = View.VISIBLE
                            binding.airConditionalOff.visibility = View.GONE
                        } else {
                            binding.airConditionalOff.visibility = View.VISIBLE
                            binding.airConditionalOn.visibility = View.GONE

                        }
                        if (it.fridge == "1") {
                            binding.fridgeOn.visibility = View.VISIBLE
                            binding.fridgeOff.visibility = View.GONE
                        } else {
                            binding.fridgeOff.visibility = View.VISIBLE
                            binding.fridgeOn.visibility = View.GONE

                        }
                        if (it.free == "1") {
                            binding.freeOn.visibility = View.VISIBLE
                            binding.freeOff.visibility = View.GONE
                        } else {
                            binding.freeOff.visibility = View.VISIBLE
                            binding.freeOn.visibility = View.GONE

                        }
                        if (it.washingMachine == "1") {
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
}
