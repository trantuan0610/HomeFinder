package com.tuantd.myapplication.mainscreen.account.RoomFavourite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.tuantd.myapplication.R
import com.tuantd.myapplication.mainscreen.home.Room
import com.tuantd.myapplication.mainscreen.home.RoomsAdapter

class FavouriteRoomActivity : AppCompatActivity() {

    private var roomList = ArrayList<Room>()
    private var roomList2 = ArrayList<Room>()
    lateinit var rcv_room: RecyclerView
    lateinit var roomsAdapter: RoomsAdapter

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("Rooms")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_room)

        rcv_room = findViewById(R.id.rcv_favRoom)
        roomsAdapter = RoomsAdapter()
        rcv_room.adapter = roomsAdapter




    }
    private fun retrieveDataFromDatabase(roomID: String) {
        myReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                roomList.clear()
                for (eachRoom in snapshot.children) {
                    val room = eachRoom.value as? HashMap<String, Any?>
                    val data = Room(
                        roomId = room?.get("roomId") as String,
                        email = room?.get("email")as String,
                        roomAddress = room["roomAddress"] as String,
                        roomImage = room["roomImage"] as String,
                        price = room["price"] as String,
                        roomArea = room["roomArea"] as String,
                        roomDescription = room["roomDescription"] as String,
                        name = room["name"] as String,
                        phone = room["phone"] as String,
                        wifi = room["phone"] as String ,
                        wc = room["phone"] as String,
                        free = room["phone"] as String,
                        fridge = room["phone"] as String,
                        airConditional = room["phone"] as String,
                        washingMachine = room["phone"] as String,
                        parking = room["parking"] as String,
                        kitchen = room["kitchen"] as String
                    )
                    roomList.add(data)
                }
                roomList.forEach {
                    if (it.roomId.equals(roomID)){
                    roomList2.add(it)
                    }

                }
                //roomsAdapter?.addList(roomList)

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}