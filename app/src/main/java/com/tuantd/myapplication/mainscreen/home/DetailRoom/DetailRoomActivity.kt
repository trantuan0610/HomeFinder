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
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tuantd.myapplication.databinding.ActivityDetailRoomBinding
import com.tuantd.myapplication.dialog.DialogSelectContact
import com.tuantd.myapplication.mainscreen.home.Room


class DetailRoomActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailRoomBinding

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
        binding = ActivityDetailRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            onBackPressed()
        }

        val roomId = intent.getStringExtra("roomId")
        if (roomId != null) {
            retrieveDataFromDatabase(roomId)
            getFavouriteRoomLike()
        }


        binding.like.setOnClickListener {
            actionLike()
        }

        binding.dontlike.setOnClickListener {
            actionDontLike()
        }
        binding.btnMore.setOnClickListener {
            DialogSelectContact(onSubmitClickListener = { it ->
                if(it ==1){
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:${roomDetail?.phone}")
                    startActivity(intent)
                }else{
                    val n = Intent(Intent.ACTION_VIEW)
                    n.type = "vnd.android-dir/mms-sms"
                    n.putExtra("address", roomDetail?.phone)
                    n.putExtra("sms_body", "Xin chào, Tôi muốn thuê phòng trọ của bạn!!!")
                    startActivity(n)
                    }
            }).show(supportFragmentManager,"tag")
        }
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

    private fun getFavouriteRoomLike() {
        myFavouriteReferene.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                roomFavList.clear()
                likeList.clear()
                for (favRoom in snapshot.children) {
                    val favRoom = favRoom.value as? HashMap<*, *>
                    val data = FavouriteRoom(
                        favouriteRoomID = favRoom?.get("favouriteRoomID") as String,
                        emailPerson = favRoom["emailPerson"] as String,
                        idRoom = favRoom["idRoom"] as String,
                        emailFav = favRoom["emailFav"] as String,
                        roomAddressFav = favRoom["roomAddressFav"] as String,
                        roomImageFav = favRoom["roomImageFav"] as String,
                        priceFav = favRoom["priceFav"] as String,
                        roomAreaFav = favRoom["roomAreaFav"] as String,
                        roomDescriptionFav = favRoom["roomDescriptionFav"] as String,
                        nameFav = favRoom["nameFav"] as String ,
                        phoneFav = favRoom["phoneFav"] as String,
                        wifiFav = favRoom["phoneFav"] as String ,
                        wcFav = favRoom["wcFav"] as String,
                        freeFav = favRoom["freeFav"] as String,
                        fridgeFav = favRoom["fridgeFav"] as String,
                        airConditionalFav = favRoom["airConditionalFav"] as String,
                        washingMachineFav = favRoom["washingMachineFav"] as String,
                        parkingFav = favRoom["parkingFav"] as String,
                        kitchenFav = favRoom["kitchenFav"] as String
                    )
                    roomFavList.add(data)
                }
                Log.e("AAAAA", "room fav list" + roomFavList.size.toString())
                roomFavList.forEach {
                    if (it.emailPerson == auth.currentUser?.email) {
                        likeList.add(it)
                    }
                }
                Log.e("AAAAA", "Like list" + likeList.size.toString())
                setLike_DontLike(likeList)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun setLike_DontLike(listFav: ArrayList<FavouriteRoom>) {
        var roomNow : FavouriteRoom ?= null
        listFav.forEach {
            if (it.idRoom.equals(roomDetail?.roomId)) {
             roomNow = it
            }
            if (roomNow!= null){
                binding.dontlike.visibility = View.VISIBLE
                binding.like.visibility =View.GONE
            }else{
                binding.dontlike.visibility = View.GONE
                binding.like.visibility = View.VISIBLE
            }

        }
    }

    // like room (done)
    private fun actionLike() {
        binding.like.visibility = View.GONE
        binding.dontlike.visibility = View.VISIBLE
        val id = myFavouriteReferene.push().key.toString()
        val favouriteRoom = FavouriteRoom(
            id,
            auth.currentUser?.email.toString(),
            roomDetail!!.roomId,
            roomDetail!!.email,
            roomDetail!!.roomAddress,
            roomDetail!!.roomImage,
            roomDetail!!.price,
            roomDetail!!.roomArea,
            roomDetail!!.roomDescription,
            roomDetail!!.name,
            roomDetail!!.phone,
            roomDetail!!.wifi,
            roomDetail!!.wc,
            roomDetail!!.free,
            roomDetail!!.fridge,
            roomDetail!!.airConditional,
            roomDetail!!.washingMachine,
            roomDetail!!.parking,
            roomDetail!!.kitchen

        )
        myFavouriteReferene.child(id).setValue(favouriteRoom).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Xong", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Cuts", Toast.LENGTH_SHORT).show()
            }
        }

    }

    //done
    private fun actionDontLike() {
        likeList.forEach {
            if (it.idRoom.equals(roomDetail?.roomId)) {
                binding.like.visibility = View.VISIBLE
                binding.dontlike.visibility = View.GONE
                myFavouriteReferene.child(it.favouriteRoomID).removeValue()
                Toast.makeText(this, "Xoa Thanh Cong", Toast.LENGTH_SHORT).show()
            } else {
                binding.like.visibility = View.GONE
                binding.dontlike.visibility = View.VISIBLE
                Toast.makeText(this, "Xoa That bai", Toast.LENGTH_SHORT).show()
            }

        }

    }
}

