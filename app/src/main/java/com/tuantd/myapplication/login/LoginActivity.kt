package com.tuantd.myapplication.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tuantd.myapplication.ListTestActivity
import com.tuantd.myapplication.R
import com.tuantd.myapplication.TestActivity
import com.tuantd.myapplication.mainscreen.MainActivity
import com.tuantd.myapplication.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var btnLogin: Button
    lateinit var btnRegister: TextView
    private lateinit var edtEmail: EditText
    private lateinit var edtPass: EditText
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
        checkLogin()
        Login()
        Register()

    }

    private fun checkLogin() {
        if (auth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun init() {
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.tv_register)
        edtEmail = findViewById(R.id.edtLoginEmail)
        edtPass = findViewById(R.id.edtLoginPass)
//        btnTest = findViewById(R.id.btnTest)
//        btnListTest =findViewById(R.id.btnListTest)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }

    private fun Login() {
        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString()
            val pass = edtPass.text.toString()

//
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                val Users = db.collection("USERS")
                val query = Users.whereEqualTo("email", email).get()
                    .addOnSuccessListener { tasks ->
                        if (tasks.isEmpty) {
                            Toast.makeText(this, "Người dùng chưa được đăng ký", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                                if (it.isSuccessful) {

                                    var intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()

                                } else {
                                    Toast.makeText(this, "Bạn nhập sai mật khẩu", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
            } else {
                Toast.makeText(this,"Email và password không được để trống",Toast.LENGTH_SHORT).show()

            }
        }
    }


    private fun Register() {
        btnRegister.setOnClickListener {
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}
