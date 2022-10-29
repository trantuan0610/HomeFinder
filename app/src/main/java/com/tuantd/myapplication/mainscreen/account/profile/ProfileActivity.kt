package com.tuantd.myapplication.mainscreen.account.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.tuantd.myapplication.databinding.ActivityProfileBinding
import com.tuantd.myapplication.mainscreen.account.ChangePassActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        setText(auth.currentUser?.email)
        auth=FirebaseAuth.getInstance()
        binding.btnChangePass.setOnClickListener {
            var intent = Intent(this , ChangePassActivity::class.java)
            startActivity(intent)
        }


    }
    private fun setText(email:String?)
    {
        db= FirebaseFirestore.getInstance()
        if (email != null) {
            db.collection("USERS").document(email).get()
                .addOnSuccessListener {
                        tasks->
                    binding.tvName.text=tasks.get("Name").toString()
                    binding.tvPhone.text=tasks.get("Phone").toString()
                    binding.tvEmail.text=tasks.get("email").toString()

                }
        }

    }
}