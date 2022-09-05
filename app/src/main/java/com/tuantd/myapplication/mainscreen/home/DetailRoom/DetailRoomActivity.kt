package com.tuantd.myapplication.mainscreen.home.DetailRoom

import android.content.Intent
import android.os.Bundle
import android.view.View
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
    lateinit var roomAddress : TextView
    lateinit var roomPrice : TextView
    lateinit var roomArea : TextView
    lateinit var roomDes : TextView
    lateinit var roomName : TextView
    lateinit var roomImg : ImageView
    lateinit var follow: ImageButton
    lateinit var unfollow: ImageButton
    lateinit var back: ImageButton

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

        back.setOnClickListener {
            onBackPressed()
        }


        val roomId = intent.getStringExtra("roomId")

        if (roomId != null) {
            retrieveDataFromDatabase(roomId)
        }
            follow.setOnClickListener{
                follow.visibility = View.GONE
                unfollow.visibility = View.VISIBLE
                var intent =Intent(this,FavouriteRoomActivity::class.java)
                intent.putExtra("roomId",roomId)
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
                        phone = room["phone"] as String

                    )
                    roomList.add(data)
                }
                roomList.forEach {
                    if (it.roomId.equals(roomID)){

                        roomAddress.text = it.roomAddress
                        roomPrice.text = it.price + "triệu"
                        roomArea.text = it.roomArea+"m2"
                        roomDes.text = it.roomDescription
                        roomName.text = it.name + "-"+ it.phone

                      Glide.with(applicationContext).load(it.roomImage).into(roomImg)

                    }

                }


                //roomsAdapter?.addList(roomList)

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


}