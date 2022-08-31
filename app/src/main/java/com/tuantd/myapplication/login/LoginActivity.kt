package com.tuantd.myapplication.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tuantd.myapplication.R
import com.tuantd.myapplication.mainscreen.MainActivity
import com.tuantd.myapplication.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var btnLogin : Button
    lateinit var btnRegister : TextView
    private lateinit var edtEmail: EditText
    private lateinit var edtPass: EditText

    lateinit var  sharedPreferences : SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("Shared_Pref", Context.MODE_PRIVATE)


        init()
        checkLogin()
        Login()
        Register()
    }

    private fun checkLogin() {
        if(auth.currentUser !=null){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun init() {
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.tv_register)
        edtEmail = findViewById(R.id.edtLoginEmail)
        edtPass = findViewById(R.id.edtLoginPass)
        auth = FirebaseAuth.getInstance()
    }
    private fun Login() {


        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString()
            val pass = edtPass.text.toString()

            val editor : SharedPreferences.Editor =sharedPreferences.edit()
           editor.putString("email",email)
            editor.apply()


            if (email.isNotEmpty() && pass.isNotEmpty() ) {
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {

                        var intent = Intent(this,MainActivity::class.java)
                        intent.putExtra("email",email)
                        startActivity(intent)
                        finish()

                    } else {
                      Toast.makeText(this,"Bạn chưa có tài khoản", Toast.LENGTH_SHORT).show()
                    }
                }
            }



        }
    }
    private fun Register() {
        btnRegister.setOnClickListener {
            var intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)

        }
    }
}
