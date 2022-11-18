package com.tuantd.myapplication.mainscreen.account.RoomFavourite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tuantd.myapplication.R
import com.tuantd.myapplication.databinding.ActivityFavouriteRoomBinding
import com.tuantd.myapplication.mainscreen.home.DetailRoom.DetailRoomActivity
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
    private val myReference: DatabaseReference = database.reference.child("favourite")
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
        roomsFavAdapter.onclickItem = {
            val intent =
                Intent(this, DetailFavouriteRoomActivity::class.java)
            intent.putExtra("roomFavId", it)
            startActivity(intent)
        }
    }
    private fun retrieveFavRoomFromDatabase() {
        myReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                favroomList.clear()
                favroomList2.clear()
                for (favRoom in snapshot.children) {
                    val favRoom = favRoom.value as? HashMap<*, *>
                    val data = FavouriteRoom(
                        id_yeu_thich = favRoom?.get("favouriteRoomID") as String,
                        id_nguoi_dung = favRoom["emailPerson"] as String,
                        id_bai_dang = favRoom["idRoom"] as String
                    )
                    favroomList.add(data)
                }
                favroomList.forEach {
                    if (it.id_nguoi_dung == auth.currentUser?.email) {
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