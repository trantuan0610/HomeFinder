package com.tuantd.myapplication.mainscreen.account.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tuantd.myapplication.databinding.ActivityChangeProfileBinding
import com.tuantd.myapplication.mainscreen.account.myRoom.MyRoomActivity
import com.tuantd.myapplication.register.User
import java.text.SimpleDateFormat
import java.util.*

class ChangeProfileActivity : AppCompatActivity() {
    lateinit var binding : ActivityChangeProfileBinding
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myUserReference: DatabaseReference = database.reference.child("user")
    var userDetail : User ?= null
    var userList = ArrayList<User>()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDataUser()
        binding.btnChange.setOnClickListener {
            changeProfile()
        }

    }

    private fun changeProfile() {
        myUserReference.child(userDetail!!.id_nguoi_dung).child("ten").setValue(binding.name.text.toString())
        myUserReference.child(userDetail!!.id_nguoi_dung).child("sdt").setValue(binding.sdt.text.toString())
        finish()
    }
    private fun getDataUser() {
        myUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (user in snapshot.children) {
                    val uSer = user.value as? HashMap<*, *>
                    val data = com.tuantd.myapplication.register.User(
                        id_nguoi_dung =  uSer?.get("id_nguoi_dung") as String,
                        email = uSer["email"] as String,
                        mat_khau = uSer["mat_khau"] as String,
                        quyen = uSer["quyen"] as String,
                        sdt = uSer["sdt"] as String,
                        ten = uSer["ten"] as String
                    )
                    userList.add(data)
                }
                userList.forEach {
                    if (it.email == auth.currentUser?.email  ) {
                        userDetail = it
                        binding.email.setText(it.email)
                        binding.name.setText(it.ten)
                        binding.sdt.setText(it.sdt)
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}