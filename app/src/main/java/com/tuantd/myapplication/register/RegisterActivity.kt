package com.tuantd.myapplication.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tuantd.myapplication.R
import com.tuantd.myapplication.login.LoginActivity
import com.tuantd.myapplication.mainscreen.MainActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var btnRegister: Button
    private lateinit var edtEmail: EditText
    private lateinit var edtPass: EditText
    private lateinit var edtName: EditText
    private lateinit var edtPhone: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnRegister = findViewById(R.id.btn_Register)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        edtEmail = findViewById(R.id.edtEmail)
        edtPass = findViewById(R.id.edtPass)
        edtName = findViewById(R.id.edtName)
        edtPhone = findViewById(R.id.edtphoneNumber)
        Register()

        btnRegister.setOnClickListener {
            if (checking()) {
                var email = edtEmail.text.toString()
                var password = edtPass.text.toString()
                var name = edtName.text.toString()
                var phone = edtPhone.text.toString()

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
        if (edtName.text.toString().trim { it <= ' ' }.isNotEmpty()
            && edtPhone.text.toString().trim { it <= ' ' }.isNotEmpty()
            && edtEmail.text.toString().trim { it <= ' ' }.isNotEmpty()
            && edtPass.text.toString().trim { it <= ' ' }.isNotEmpty()
        ) {
            return true
        }
        return false
    }


    private fun Register() {
        btnRegister.setOnClickListener {
            val email = edtEmail.text.toString()
            val pass = edtPass.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
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


}


