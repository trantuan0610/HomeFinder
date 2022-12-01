package com.tuantd.myapplication.login

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.tuantd.myapplication.ListTestActivity
import com.tuantd.myapplication.R
import com.tuantd.myapplication.TestActivity
import com.tuantd.myapplication.mainscreen.MainActivity
import com.tuantd.myapplication.register.RegisterActivity
import com.tuantd.myapplication.register.User

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    lateinit var btnLogin: Button
    lateinit var btnTest: Button
    lateinit var btnRegister: TextView
    private lateinit var edtEmail: EditText
    private lateinit var edtPass: EditText
    private lateinit var db: FirebaseFirestore
    private val myUserReference: DatabaseReference = database.reference.child("user")
    private var userDetail: User? = null
    val userList = java.util.ArrayList<User>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        createDialog()
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
        btnTest = findViewById(R.id.btnTest)
//        btnListTest =findViewById(R.id.btnListTest)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }

    private fun Login() {
        btnLogin.setOnClickListener {

            val email = edtEmail.text.toString()
            val pass = edtPass.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                showLoading()
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {

                        var intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        hiddenLoading()
                        finish()

                    } else if (!getUserSuccessful()) {
                        Toast.makeText(this, "Bạn nhập sai mật khẩu", Toast.LENGTH_SHORT).show()
                        hiddenLoading()

                        }else{
                        Toast.makeText(this, "Ban chưa có tài khoản.Hãy đăng kí tài khoản để tiếp tục!", Toast.LENGTH_SHORT).show()
                        hiddenLoading()
                        }

                }
            } else {
                Toast.makeText(this, "Không được bỏ trống email và mật khẩu", Toast.LENGTH_SHORT).show()
                hiddenLoading()
            }
        }

        btnTest.setOnClickListener {
            var intent = Intent(this,TestActivity::class.java)
            startActivity(intent)
        }
    }


    private fun Register() {
        btnRegister.setOnClickListener {
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getUserSuccessful() :Boolean{
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
                    if (it.email == edtEmail.text.toString()){
                        userDetail = it
                    }
                }
            }


            override fun onCancelled(error: DatabaseError) {
            }
        })

        return userDetail!=null
    }

    //loading
    private var dialog: AlertDialog? = null

    private fun createDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.layout_loading, null)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        dialog = dialogBuilder.create()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(true)
    }

    fun showLoading() {
        dialog?.show()
    }

    fun hiddenLoading() {
        dialog?.dismiss()
    }
    //

    override fun onBackPressed() {
        super.onBackPressed()
    }

}
