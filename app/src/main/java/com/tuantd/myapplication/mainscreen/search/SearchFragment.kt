package com.tuantd.myapplication.mainscreen.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.tuantd.myapplication.R
import com.tuantd.myapplication.mainscreen.MainActivity
import com.tuantd.myapplication.mainscreen.home.DetailRoom.DetailRoomActivity
import com.tuantd.myapplication.mainscreen.home.Room
import com.tuantd.myapplication.mainscreen.home.RoomsAdapter


class SearchFragment : Fragment() {
    private var roomList = ArrayList<Room>()
    private var arrayList = ArrayList<Room>()
    lateinit var rcv_searchRoom: RecyclerView
    private var roomsAdapter: RoomsAdapter?=null
    lateinit var searchView: SearchView
    lateinit var tvHistory: TextView

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("Rooms")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        rcv_searchRoom = view.findViewById(R.id.rcv_searchRooms)
        searchView = view.findViewById(R.id.searchView)
        tvHistory = view.findViewById(R.id.tvHiss)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retrieveDataFromDatabase()
        roomsAdapter = RoomsAdapter()
        rcv_searchRoom.adapter = roomsAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                    tvHistory.visibility =View.GONE
                if (newText!!.isNotEmpty()) {
                    arrayList.clear()
                    val search = newText.toString().lowercase()
                    roomList.forEach {
                        if (it.roomAddress.lowercase().contains(search)) {
                            arrayList.add(it)

                        }
                    }
                    roomsAdapter?.addList(arrayList)
                    roomsAdapter!!.onclickItem = {
                        val intent =
                            Intent((activity as MainActivity), DetailRoomActivity::class.java)
                        intent.putExtra("roomId", it)
                        (activity as MainActivity).startActivity(intent)
                    }
                } else {
                    arrayList.clear()
                    tvHistory.visibility = View.VISIBLE
//                    arrayList.addAll(roomList)
//                    roomsAdapter?.addList(arrayList)
                }

                return true
            }

        })
    }

    private fun retrieveDataFromDatabase() {
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
                //roomsAdapter?.addList(roomList)

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}