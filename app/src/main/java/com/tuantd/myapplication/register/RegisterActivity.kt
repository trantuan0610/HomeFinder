package com.tuantd.myapplication.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.tuantd.myapplication.R
import com.tuantd.myapplication.databinding.ActivityRegisterBinding
import com.tuantd.myapplication.login.LoginActivity
import com.tuantd.myapplication.mainscreen.MainActivity
import com.tuantd.myapplication.mainscreen.home.DetailRoom.FavouriteRoom

class RegisterActivity : AppCompatActivity() {
    private  var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private  var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var dbrt: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myUserReferene: DatabaseReference = dbrt.reference.child("user")

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        Register()

        binding.btnRegister.setOnClickListener {
            if (checking()) {
                var email = binding.edtEmail.text.toString()
                var password = binding.edtPass.text.toString()
                var name = binding.edtName.text.toString()
                var phone = binding.edtphoneNumber.text.toString()

                val user = hashMapOf(
                    "Name" to name,
                    "Phone" to phone,
                    "email" to email
                )

                val Users = db.collection("USERS")
                val query = Users.whereEqualTo("email", email).get()
                    .addOnSuccessListener { tasks ->
                        if (tasks.isEmpty) {
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        Users.document(email).set(user)
                                        //
                                        val id =  myUserReferene.push().key.toString()
                                        val user1 = User(
                                            id,
                                            email,
                                            password,
                                            "0",
                                            phone,
                                            name
                                        )
                                        myUserReferene.child(id).setValue(user1).addOnCompleteListener { task ->
                                            if (task.isSuccessful) {

                                            } else {

                                            }
                                        }
                                        //
                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Authentication Failed",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                        } else {
                            Toast.makeText(this, "Người dùng đã được đăng ký", Toast.LENGTH_LONG)
                                .show()
                        }
                    }


            } else {
                Toast.makeText(this, "Enter the Details", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun checking(): Boolean {
        if (binding.edtName.text.toString().trim { it <= ' ' }.isNotEmpty()
            && binding.edtphoneNumber.text.toString().trim { it <= ' ' }.isNotEmpty()
            && binding.edtEmail.text.toString().trim { it <= ' ' }.isNotEmpty()
            && binding.edtPass.text.toString().trim { it <= ' ' }.isNotEmpty()
        ) {
            return true
        }
        return false
    }


    private fun Register() {
        binding.btnRegister.setOnClickListener {

            if (binding.edtPass.text.isNotEmpty() && binding.edtEmail.text.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(binding.edtEmail.text.toString(), binding.edtPass.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Thiếu thông tin", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}


