package com.tuantd.myapplication.mainscreen.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.tuantd.myapplication.R
import com.tuantd.myapplication.mainscreen.MainActivity
import com.tuantd.myapplication.mainscreen.home.AddRoom.AddRoomActivity
import com.tuantd.myapplication.mainscreen.home.DetailRoom.DetailRoomActivity

class HomeFragment : Fragment() {
    private var loadDone:(() -> Unit)?=null
    private var roomList = ArrayList<Room>()
    lateinit var rcv_room: RecyclerView
    private val roomsAdapter= RoomsAdapter()
    lateinit var btnAdd: FloatingActionButton

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("Rooms")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rcv_room = view.findViewById(R.id.rcv_room)
        btnAdd = view.findViewById(R.id.btn_Add)

        btnAdd.setOnClickListener {
            val intent = Intent(activity as MainActivity, AddRoomActivity::class.java)
            startActivity(intent)
        }
        retrieveDataFromDatabase()

        loadDone={
            roomsAdapter.addList(roomList)
        }
        rcv_room.adapter = roomsAdapter

        roomsAdapter.onclickItem = {
            val intent =
                Intent((activity as MainActivity), DetailRoomActivity::class.java)
            intent.putExtra("roomId", it)
            (activity as MainActivity).startActivity(intent)
        }
    }

    private fun retrieveDataFromDatabase() {
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
                        name = room["name"] as String ,
                        phone = room["phone"] as String,
                        wifi = room["phone"] as String ,
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
                loadDone?.invoke()

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }




}



