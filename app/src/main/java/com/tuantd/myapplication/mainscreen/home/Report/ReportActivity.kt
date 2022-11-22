package com.tuantd.myapplication.mainscreen.home.Report

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tuantd.myapplication.databinding.ActivityReportBinding
import com.tuantd.myapplication.register.User
import java.text.SimpleDateFormat
import java.util.Date

class ReportActivity : AppCompatActivity() {
    lateinit var binding: ActivityReportBinding
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("report")
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val myUserReference: DatabaseReference = database.reference.child("user")
    private var userDetail: User? = null
    val userList = java.util.ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getUser()
        val roomId = intent.getStringExtra("roomId")
        binding.btnReport.setOnClickListener {
            if (roomId!=null){
                report(roomId)
            }
        }



    }
    private fun getUser() {
        myUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (user in snapshot.children) {
                    val uSer = user.value as? java.util.HashMap<*, *>
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
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    private fun report(roomId: String?) {
        val id = myReference.push().key.toString()
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        val date = Date()
        val current = formatter.format(date)

        val report = Report(
            id,
            roomId!!,
            userDetail!!.id_nguoi_dung,
            binding.edtTitle.text.toString()+ " " ,
            binding.edtContent.text.toString() + " ",
            current.toString()
        )
        myReference.child(id).setValue(report).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Báo cáo thành công.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                finish()
            }
        }
    }
}