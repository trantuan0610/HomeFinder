package com.tuantd.myapplication.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tuantd.myapplication.databinding.DialogRateBinding
import com.tuantd.myapplication.mainscreen.home.Rate.Rate
import com.tuantd.myapplication.register.User
import java.util.ArrayList
import java.util.HashMap

class DialogRate(private val idRoom: String?,private val onSubmitClickListener: (Int) -> Unit) : DialogFragment() {
    private lateinit var binding: DialogRateBinding
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("rate")
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val myUserReference: DatabaseReference = database.reference.child("user")
    private var userDetail: User? = null
    val userList = ArrayList<User>()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogRateBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)
        getUser()
        var sodiem = 0
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        binding.btnOk.setOnClickListener {
            if (sodiem != 0){
                rate(sodiem)
                Log.e("SODIEM",sodiem.toString())
                onSubmitClickListener.invoke(1)
                dismiss()
            }else{
                Toast.makeText(requireActivity(), "Bạn hãy chọn sao cho bài đăng này!", Toast.LENGTH_SHORT).show()
            }

        }

        binding.unvote1.setOnClickListener {
            unvote1()
            sodiem = 1
//            onSubmitClickListener.invoke(1)

        }
        binding.unvote2.setOnClickListener {
            unvote2()
            sodiem = 2
//            onSubmitClickListener.invoke(2)
        }
        binding.unvote3.setOnClickListener {
            unvote3()
            sodiem = 3
//            onSubmitClickListener.invoke(3)
        }
        binding.unvote4.setOnClickListener {
            unvote4()
            sodiem = 4
//            onSubmitClickListener.invoke(4)
        }
        binding.unvote5.setOnClickListener {
            unvote5()
            sodiem = 5
//            onSubmitClickListener.invoke(5)
        }
        binding.vote1.setOnClickListener {
            vote1()
            sodiem= 1
//            onSubmitClickListener.invoke(1)
        }
        binding.vote2.setOnClickListener {
            vote2()
            sodiem = 2
//            onSubmitClickListener.invoke(2)
        }
        binding.vote3.setOnClickListener {
            vote3()
            sodiem = 3
//            onSubmitClickListener.invoke(3)
        }
        binding.vote4.setOnClickListener {
            vote4()
            sodiem = 4
//            onSubmitClickListener.invoke(4)
        }
        binding.vote5.setOnClickListener {
            vote5()
            sodiem = 5
//            onSubmitClickListener.invoke(5)
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.gravity = Gravity.BOTTOM
        return dialog
    }

    private fun getUser() {
        myUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (user in snapshot.children) {
                    val uSer = user.value as? HashMap<*, *>
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

    private fun rate(diem: Int?) {
        val id = myReference.push().key.toString()

        val rate = Rate(
            id,
            idRoom!!,
            userDetail!!.id_nguoi_dung,
            so_diem = diem.toString()
        )
        myReference.child(id).setValue(rate).addOnCompleteListener { task ->
            if (task.isSuccessful) {

            } else {

            }
        }
    }

    private fun vote5() {
        binding.vote1.visibility = View.VISIBLE
        binding.vote2.visibility = View.VISIBLE
        binding.vote3.visibility = View.VISIBLE
        binding.vote4.visibility = View.VISIBLE
        binding.vote5.visibility = View.VISIBLE
        binding.unvote1.visibility = View.GONE
        binding.unvote2.visibility = View.GONE
        binding.unvote3.visibility = View.GONE
        binding.unvote4.visibility = View.GONE
        binding.unvote5.visibility = View.GONE
    }

    private fun vote4() {
        binding.vote1.visibility = View.VISIBLE
        binding.vote2.visibility = View.VISIBLE
        binding.vote3.visibility = View.VISIBLE
        binding.vote4.visibility = View.VISIBLE
        binding.vote5.visibility = View.GONE
        binding.unvote1.visibility = View.GONE
        binding.unvote2.visibility = View.GONE
        binding.unvote3.visibility = View.GONE
        binding.unvote4.visibility = View.GONE
        binding.unvote5.visibility = View.VISIBLE
    }

    private fun vote3() {
        binding.vote1.visibility = View.VISIBLE
        binding.vote2.visibility = View.VISIBLE
        binding.vote3.visibility = View.VISIBLE
        binding.vote4.visibility = View.GONE
        binding.vote5.visibility = View.GONE
        binding.unvote1.visibility = View.GONE
        binding.unvote2.visibility = View.GONE
        binding.unvote3.visibility = View.GONE
        binding.unvote4.visibility = View.VISIBLE
        binding.unvote5.visibility = View.VISIBLE
    }

    private fun vote2() {
        binding.vote1.visibility = View.VISIBLE
        binding.vote2.visibility = View.VISIBLE
        binding.vote3.visibility = View.GONE
        binding.vote4.visibility = View.GONE
        binding.vote5.visibility = View.GONE
        binding.unvote1.visibility = View.GONE
        binding.unvote2.visibility = View.GONE
        binding.unvote3.visibility = View.VISIBLE
        binding.unvote4.visibility = View.VISIBLE
        binding.unvote5.visibility = View.VISIBLE
    }

    private fun vote1() {
        binding.vote1.visibility = View.VISIBLE
        binding.vote2.visibility = View.GONE
        binding.vote3.visibility = View.GONE
        binding.vote4.visibility = View.GONE
        binding.vote5.visibility = View.GONE
        binding.unvote1.visibility = View.GONE
        binding.unvote2.visibility = View.VISIBLE
        binding.unvote3.visibility = View.VISIBLE
        binding.unvote4.visibility = View.VISIBLE
        binding.unvote5.visibility = View.VISIBLE
    }

    private fun unvote5() {
        binding.vote1.visibility = View.VISIBLE
        binding.vote2.visibility = View.VISIBLE
        binding.vote3.visibility = View.VISIBLE
        binding.vote4.visibility = View.VISIBLE
        binding.vote5.visibility = View.VISIBLE
        binding.unvote1.visibility = View.GONE
        binding.unvote2.visibility = View.GONE
        binding.unvote3.visibility = View.GONE
        binding.unvote4.visibility = View.GONE
        binding.unvote5.visibility = View.GONE
    }

    private fun unvote4() {
        binding.vote1.visibility = View.VISIBLE
        binding.vote2.visibility = View.VISIBLE
        binding.vote3.visibility = View.VISIBLE
        binding.vote4.visibility = View.VISIBLE
        binding.vote5.visibility = View.GONE
        binding.unvote1.visibility = View.GONE
        binding.unvote2.visibility = View.GONE
        binding.unvote3.visibility = View.GONE
        binding.unvote4.visibility = View.GONE
        binding.unvote5.visibility = View.VISIBLE
    }

    private fun unvote3() {
        binding.vote1.visibility = View.VISIBLE
        binding.vote2.visibility = View.VISIBLE
        binding.vote3.visibility = View.VISIBLE
        binding.vote4.visibility = View.GONE
        binding.vote5.visibility = View.GONE
        binding.unvote1.visibility = View.GONE
        binding.unvote2.visibility = View.GONE
        binding.unvote3.visibility = View.GONE
        binding.unvote4.visibility = View.VISIBLE
        binding.unvote5.visibility = View.VISIBLE
    }

    private fun unvote2() {
        binding.vote1.visibility = View.VISIBLE
        binding.vote2.visibility = View.VISIBLE
        binding.vote3.visibility = View.GONE
        binding.vote4.visibility = View.GONE
        binding.vote5.visibility = View.GONE
        binding.unvote1.visibility = View.GONE
        binding.unvote2.visibility = View.GONE
        binding.unvote3.visibility = View.VISIBLE
        binding.unvote4.visibility = View.VISIBLE
        binding.unvote5.visibility = View.VISIBLE
    }

    private fun unvote1() {
        binding.vote1.visibility = View.VISIBLE
        binding.vote2.visibility = View.GONE
        binding.vote3.visibility = View.GONE
        binding.vote4.visibility = View.GONE
        binding.vote5.visibility = View.GONE
        binding.unvote1.visibility = View.GONE
        binding.unvote2.visibility = View.VISIBLE
        binding.unvote3.visibility = View.VISIBLE
        binding.unvote4.visibility = View.VISIBLE
        binding.unvote5.visibility = View.VISIBLE
    }
}