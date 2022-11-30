package com.tuantd.myapplication.mainscreen.account.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.tuantd.myapplication.databinding.ActivityProfileBinding
import com.tuantd.myapplication.mainscreen.account.ChangePassActivity
import com.tuantd.myapplication.register.User

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    private val myUserReference: DatabaseReference = database.reference.child("user")
    private var userDetail: User? = null
    val userList = java.util.ArrayList<User>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        getUser()
        binding.btnChangePass.setOnClickListener {
            var intent = Intent(this , ChangePassActivity::class.java)
            startActivity(intent)
        }
        binding.btnChangeProfile.setOnClickListener {
            var intent = Intent(this , ChangeProfileActivity::class.java)
            startActivity(intent)
        }


    }


    private fun getUser()
    {
        myUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (user in snapshot.children) {
                    val uSer = user.value as? java.util.HashMap<*, *>
                    val data = User(
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
                        binding.tvName.text = userDetail!!.ten
                        binding.tvEmail.text = userDetail!!.email
                        binding.tvPhone.text = userDetail!!.sdt

                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }
}