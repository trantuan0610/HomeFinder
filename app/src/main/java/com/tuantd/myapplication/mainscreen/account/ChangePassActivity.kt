package com.tuantd.myapplication.mainscreen.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.tuantd.myapplication.R
import com.tuantd.myapplication.databinding.ActivityChangePassBinding
import com.tuantd.myapplication.login.LoginActivity

class ChangePassActivity : AppCompatActivity() {
    lateinit var binding : ActivityChangePassBinding
    var auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnChangePassword.setOnClickListener {
            changePass()
        }
        binding.back.setOnClickListener {
            onBackPressed()
        }

    }

    private fun changePass() {
        if (binding.currentPassword.text.isNotEmpty() &&
            binding.newPassword.text.isNotEmpty() &&
            binding.confirmPassword.text.isNotEmpty()
        ) {

            if (binding.newPassword.text.toString() == binding.confirmPassword.text.toString()
            ) {

                val user = auth.currentUser
                if (user != null && user.email != null) {
                    val credential = EmailAuthProvider
                        .getCredential(user.email!!, binding.currentPassword.text.toString())

                    user.reauthenticate(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {

                                user.updatePassword(binding.newPassword.text.toString())
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