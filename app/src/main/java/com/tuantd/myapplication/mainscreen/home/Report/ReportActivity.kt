package com.tuantd.myapplication.mainscreen.home.Report

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tuantd.myapplication.databinding.ActivityReportBinding
import java.text.SimpleDateFormat
import java.util.Date

class ReportActivity : AppCompatActivity() {
    lateinit var binding: ActivityReportBinding
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("report")
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val roomId = intent.getStringExtra("roomId")
        binding.btnReport.setOnClickListener {
            if (roomId!=null){
                report(roomId)
            }
        }



    }

    private fun report(roomId: String?) {
        val id = myReference.push().key.toString()
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        val date = Date()
        val current = formatter.format(date)

        val report = Report(
            id,
            roomId!!,
            auth.currentUser?.email.toString(),
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