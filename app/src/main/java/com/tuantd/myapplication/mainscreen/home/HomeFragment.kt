package com.tuantd.myapplication.mainscreen.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.tuantd.myapplication.R
import com.tuantd.myapplication.mainscreen.MainActivity
import com.tuantd.myapplication.mainscreen.home.AddRoom.AddRoomActivity

class HomeFragment : Fragment() {

    private var roomList = ArrayList<Room>()
    lateinit var rcv_room: RecyclerView
    lateinit var roomsAdapter: RoomsAdapter
    lateinit var btnAdd: FloatingActionButton

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("Rooms")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        rcv_room = view.findViewById(R.id.rcv_room)
        btnAdd = view.findViewById(R.id.btn_Add)

        btnAdd.setOnClickListener {
            val intent = Intent(activity as MainActivity, AddRoomActivity::class.java)
            startActivity(intent)
        }
        retrieveDataFromDatabase()

        return view
    }

    private fun retrieveDataFromDatabase() {
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

                    roomsAdapter = RoomsAdapter(requireContext(), roomList)
                    rcv_room.adapter = roomsAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }


}



