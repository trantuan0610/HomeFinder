package com.tuantd.myapplication.mainscreen.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.tuantd.myapplication.R
import com.tuantd.myapplication.login.LoginActivity

class ChangePassActivity : AppCompatActivity() {

    lateinit var currentPass: EditText
    lateinit var newPass: EditText
    lateinit var new2Pass: EditText
    lateinit var btnEdit: Button
    var auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pass)

        currentPass = findViewById(R.id.currentPassword)
        newPass = findViewById(R.id.newPassword)
        new2Pass = findViewById(R.id.confirmPassword)
        btnEdit = findViewById(R.id.btnChangePassword)

        btnEdit.setOnClickListener {

            changePass()

        }


    }

    private fun changePass() {
        if (currentPass.text.isNotEmpty() &&
            newPass.text.isNotEmpty() &&
            new2Pass.text.isNotEmpty()
        ) {

            if (newPass.text.toString() == new2Pass.text.toString()
            ) {

                val user = auth.currentUser
                if (user != null && user.email != null) {
                    val credential = EmailAuthProvider
                        .getCredential(user.email!!, currentPass.text.toString())

                    user.reauthenticate(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {

                                user.updatePassword(newPass.text.toString())
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(
                                                this,
                                                "Thay đổi password thành công",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                        }
                                        finish()
                                    }

                            } else {
                            }
                        }
                }

            } else {
                Toast.makeText(this, "Mật Khẩu không trùng khớp", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(this, "Không được bỏ trống ", Toast.LENGTH_SHORT).show()
        }
    }

    private fun LogOut() {
        FirebaseAuth.getInstance().signOut()
        var intent: Intent
        intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}