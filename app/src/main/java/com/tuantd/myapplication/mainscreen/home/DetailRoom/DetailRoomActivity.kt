package com.tuantd.myapplication.mainscreen.home.DetailRoom

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.tuantd.myapplication.R
import com.tuantd.myapplication.RoomFavourite.FavouriteRoomActivity
import com.tuantd.myapplication.mainscreen.home.Room

class DetailRoomActivity : AppCompatActivity() {

    private var roomList = ArrayList<Room>()
    lateinit var roomAddress: TextView
    lateinit var roomPrice: TextView
    lateinit var roomArea: TextView
    lateinit var roomDes: TextView
    lateinit var roomName: TextView
    lateinit var roomImg: ImageView
    lateinit var follow: ImageButton
    lateinit var unfollow: ImageButton
    lateinit var back: ImageButton

    lateinit var btn_wifiOn: ImageButton
    lateinit var btn_wifiOf: ImageButton
    lateinit var btn_vesinhOn: ImageButton
    lateinit var btn_vesinhOf: ImageButton
    lateinit var btn_dieuhoaOn: ImageButton
    lateinit var btn_dieuhoaOf: ImageButton
    lateinit var btn_maygiatOn: ImageButton
    lateinit var btn_maygiatOf: ImageButton
    lateinit var btn_tudoOf: ImageButton
    lateinit var btn_tudoOn: ImageButton
    lateinit var btn_giuxeOf: ImageButton
    lateinit var btn_giuxeOn: ImageButton
    lateinit var btn_bepOf: ImageButton
    lateinit var btn_bepOn: ImageButton
    lateinit var btn_tulanhOn: ImageButton
    lateinit var btn_tulanhOf: ImageButton


    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("Rooms")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_room)

        roomAddress = findViewById(R.id.tvRoomAddress)
        roomPrice = findViewById(R.id.tvPrice)
        roomArea = findViewById(R.id.tvRoomArea)
        roomDes = findViewById(R.id.tvDetailDes)
        roomImg = findViewById(R.id.imgRoom)
        roomName = findViewById(R.id.tvName)
        follow = findViewById(R.id.like)
        unfollow = findViewById(R.id.dontlike)
        back = findViewById(R.id.back)

        btn_wifiOn = findViewById(R.id.wifi_on)
        btn_wifiOf = findViewById(R.id.wifi_off)
        btn_vesinhOn = findViewById(R.id.wc_on)
        btn_vesinhOf = findViewById(R.id.wc_off)
        btn_dieuhoaOn = findViewById(R.id.airConditional_on)
        btn_dieuhoaOf = findViewById(R.id.airConditional_off)
        btn_maygiatOn = findViewById(R.id.washingMachine_on)
        btn_maygiatOf = findViewById(R.id.washingMachine_off)
        btn_tudoOf = findViewById(R.id.free_off)
        btn_tudoOn = findViewById(R.id.free_on)
        btn_giuxeOf = findViewById(R.id.parking_off)
        btn_giuxeOn = findViewById(R.id.parking_on)
        btn_bepOf = findViewById(R.id.kitchen_off)
        btn_bepOn = findViewById(R.id.kitchen_on)
        btn_tulanhOf = findViewById(R.id.fridge_off)
        btn_tulanhOn = findViewById(R.id.fridge_on)

        back.setOnClickListener {
            onBackPressed()
        }


        val roomId = intent.getStringExtra("roomId")

        if (roomId != null) {
            retrieveDataFromDatabase(roomId)
        }
        follow.setOnClickListener {
            follow.visibility = View.GONE
            unfollow.visibility = View.VISIBLE
            var intent = Intent(this, FavouriteRoomActivity::class.java)
            intent.putExtra("roomId", roomId)
        }
    }


    private fun retrieveDataFromDatabase(roomID: String) {
        myReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                roomList.clear()
                for (eachRoom in snapshot.children) {
                    val room = eachRoom.value as? HashMap<String, Any?>
                    val data = Room(
                        roomId = room?.get("roomId") as String,
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

                        roomAddress.text = it.roomAddress
                        roomPrice.text = it.price + "triệu"
                        roomArea.text = it.roomArea + "m2"
                        roomDes.text = it.roomDescription
                        roomName.text = it.name + "-" + it.phone

                        Glide.with(applicationContext).load(it.roomImage).into(roomImg)

                        if(it.wifi == "1"){
                            btn_wifiOn.visibility = View.VISIBLE
                            btn_wifiOf.visibility = View.GONE
                        }else{
                            btn_wifiOf.visibility = View.VISIBLE
                            btn_wifiOn.visibility = View.GONE

                        }
                        if(it.wc == "1"){
                            btn_vesinhOn.visibility = View.VISIBLE
                            btn_vesinhOf.visibility = View.GONE
                        }else{
                            btn_vesinhOn.visibility = View.GONE
                            btn_vesinhOf.visibility = View.VISIBLE

                        }
                        if(it.kitchen == "1"){
                            btn_bepOn.visibility = View.VISIBLE
                            btn_bepOf.visibility = View.GONE
                        }else{
                            btn_bepOf.visibility = View.VISIBLE
                            btn_bepOn.visibility = View.GONE

                        }
                        if(it.parking == "1"){
                            btn_giuxeOn.visibility = View.VISIBLE
                            btn_giuxeOf.visibility = View.GONE
                        }else{
                            btn_giuxeOf.visibility = View.VISIBLE
                            btn_giuxeOn.visibility = View.GONE

                        }
                        if(it.airConditional == "1"){
                            btn_dieuhoaOn.visibility = View.VISIBLE
                            btn_dieuhoaOf.visibility = View.GONE
                        }else{
                            btn_dieuhoaOf.visibility = View.VISIBLE
                            btn_dieuhoaOn.visibility = View.GONE

                        }
                        if(it.fridge == "1"){
                            btn_tulanhOn.visibility = View.VISIBLE
                            btn_tulanhOf.visibility = View.GONE
                        }else{
                            btn_tulanhOf.visibility = View.VISIBLE
                            btn_tulanhOn.visibility = View.GONE

                        }
                        if(it.free == "1"){
                            btn_tudoOn.visibility = View.VISIBLE
                            btn_tudoOf.visibility = View.GONE
                        }else{
                            btn_tudoOf.visibility = View.VISIBLE
                            btn_tudoOn.visibility = View.GONE

                        }
                        if(it.washingMachine == "1"){
                            btn_maygiatOn.visibility = View.VISIBLE
                            btn_maygiatOf.visibility = View.GONE
                        }else{
                            btn_maygiatOf.visibility = View.VISIBLE
                            btn_maygiatOn.visibility = View.GONE

                        }


                    }

                }


                //roomsAdapter?.addList(roomList)

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


}