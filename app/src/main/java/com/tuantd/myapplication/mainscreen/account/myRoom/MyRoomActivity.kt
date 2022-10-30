package com.tuantd.myapplication.mainscreen.account.myRoom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tuantd.myapplication.databinding.ActivityMyRoomBinding
import com.tuantd.myapplication.mainscreen.home.DetailRoom.DetailRoomActivity
import com.tuantd.myapplication.mainscreen.home.Room
import com.tuantd.myapplication.mainscreen.home.RoomsAdapter

class MyRoomActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyRoomBinding
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("Rooms")
    private var roomList = ArrayList<Room>()
    private var arrayList = ArrayList<Room>()
    private var roomsAdapter = RoomsAdapter()
    private var loadDone:(() -> Unit)?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retrieveDataFromDatabase(FirebaseAuth.getInstance().currentUser!!.email.toString())
        loadDone={
            roomsAdapter?.addList(arrayList)
        }
        binding.rcvRoom.adapter = roomsAdapter
        roomsAdapter.onclickItem = {
            val intent =
                Intent(this, DetailRoomActivity::class.java)
            intent.putExtra("roomId", it)
            startActivity(intent)
        }
    }

    private fun retrieveDataFromDatabase(email: String) {
        myReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                roomList.clear()
                for (eachRoom in snapshot.children) {
                    val room = eachRoom.value as? HashMap<String, Any?>
                    val data = Room(
                        roomId = room?.get("roomId") as String,
                        email = room?.get("email") as String,
                        roomAddress = room["roomAddress"] as String,
                        roomImage = room["roomImage"] as String,
                        price = room["price"] as String,
                        roomArea = room["roomArea"] as String,
                        roomDescription = room["roomDescription"] as String,
                        name = room["name"] as String,
                        phone = room["phone"] as String,
                        wifi = room["phone"] as String,
                        wc = room["phone"] as String,
                        free = room["phone"] as String,
                        fridge = room["phone"] as String,
                        airConditional = room["phone"] as String,
                        washingMachine = room["phone"] as String,
                        parking = room["phone"] as String,
                        kitchen = room["phone"] as String
                    )
                    roomList.add(data)
                }
                fillDataFromFireBase(email)
                loadDone?.invoke()
                //roomsAdapter?.addList(roomList)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun fillDataFromFireBase(email: String) {
        roomList.forEach {
            if (it.email == email) {
                arrayList.add(it)
            }
        }
        roomsAdapter?.addList(arrayList)

    }
}