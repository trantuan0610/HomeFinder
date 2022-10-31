package com.tuantd.myapplication.mainscreen.account.RoomFavourite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tuantd.myapplication.R
import com.tuantd.myapplication.databinding.ActivityFavouriteRoomBinding
import com.tuantd.myapplication.mainscreen.home.DetailRoom.FavouriteRoom
import com.tuantd.myapplication.mainscreen.home.Room
import com.tuantd.myapplication.mainscreen.home.RoomsAdapter

class FavouriteRoomActivity : AppCompatActivity() {

    private var loadDone:(() -> Unit)?=null
    private var favroomList = ArrayList<FavouriteRoom>()
    private var favroomList2 = ArrayList<FavouriteRoom>()
     var roomsFavAdapter= FavouriteRoomAdapter()
    lateinit var binding : ActivityFavouriteRoomBinding
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("MyFavouriteRoom")
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)


        retrieveFavRoomFromDatabase()

        loadDone={
            roomsFavAdapter.addList(favroomList2)
            Log.e("TUAN",favroomList2.size.toString())
        }

        binding.rcvFavRoom.adapter = roomsFavAdapter
    }
    private fun retrieveFavRoomFromDatabase() {
        myReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                favroomList.clear()
                favroomList2.clear()
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
                    favroomList.add(data)
                }
                favroomList.forEach {
                    if (it.emailPerson == auth.currentUser?.email) {
                        favroomList2.add(it)
                    }
                }
                loadDone?.invoke()

            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}