package com.tuantd.myapplication.mainscreen.home.DetailRoom

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.tuantd.myapplication.R
import com.tuantd.myapplication.mainscreen.home.Room

class DetailRoomActivity : AppCompatActivity() {

    private var roomList = ArrayList<Room>()
    lateinit var roomAddress : TextView
    lateinit var roomPrice : TextView
    lateinit var roomArea : TextView
    lateinit var roomDes : TextView
    lateinit var roomImg : ImageView
    lateinit var context: Context

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

        var roomId = intent.getStringExtra("roomId")

        if (roomId != null) {
            retrieveDataFromDatabase(roomId)
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
                        roomDescription = room["roomDescription"] as String
                    )
                    roomList.add(data)
                }
                roomList.forEach {
                    if (it.roomId.equals(roomID)){
                        roomAddress.text = it.roomAddress
                        roomPrice.text = it.price + "triệu"
                        roomArea.text = it.roomArea+"m2"
                        roomDes.text = it.roomDescription

                      Glide.with(this@DetailRoomActivity).load(it.roomImage).into(roomImg)

                    }

                }
                //roomsAdapter?.addList(roomList)

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}