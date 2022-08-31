package com.tuantd.myapplication.mainscreen.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.tuantd.myapplication.R
import com.tuantd.myapplication.mainscreen.home.Room
import com.tuantd.myapplication.mainscreen.home.RoomsAdapter


class SearchFragment : Fragment() {
    private var roomList = ArrayList<Room>()
    private var arrayList = ArrayList<Room>()
    lateinit var rcv_searchRoom: RecyclerView
    private var roomsAdapter: RoomsAdapter?=null
    lateinit var searchView: SearchView

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









        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retrieveDataFromDatabase()
        roomsAdapter = RoomsAdapter(requireContext(), arrayList)
        rcv_searchRoom.adapter = roomsAdapter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    arrayList.clear()
                    val search = newText.toString()
                    roomList.forEach {
                        if (it.roomAddress.contains(search)) {
                            arrayList.add(it)

                        }
                    }
                    roomsAdapter?.addList(arrayList)

                } else {
                    arrayList.clear()
                    arrayList.addAll(roomList)
                    roomsAdapter?.addList(arrayList)
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
                        roomAddress = room["roomAddress"] as String,
                        roomImage = room["roomImage"] as String,
                        price = room["price"] as String,
                        roomArea = room["roomArea"] as String,
                        roomDescription = room["roomDescription"] as String
                    )
                    roomList.add(data)
                }
                roomsAdapter?.addList(roomList)

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}