package com.tuantd.myapplication.mainscreen.account

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tuantd.myapplication.R
import com.tuantd.myapplication.RoomFavourite.FavouriteRoomActivity
import com.tuantd.myapplication.login.LoginActivity


class AccountFragment : Fragment() {

    lateinit var btnLogOut: Button
    lateinit var tvName: TextView
    lateinit var tvEmail: TextView
    lateinit var tvPhone: TextView
    lateinit var imgMore: ImageButton
    lateinit var btnChangePass: Button
    private lateinit var db: FirebaseFirestore




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arguments
        var email = data!!.get("email").toString()
        setText(email)



        btnLogOut = view.findViewById(R.id.btnLogOut)
        tvName = view.findViewById(R.id.tvName)
        tvEmail = view.findViewById(R.id.tvEmail)
        tvPhone = view.findViewById(R.id.tvPhone)
        imgMore = view.findViewById(R.id.imgMore)
        btnChangePass = view.findViewById(R.id.btnChangePass)
        btnLogOut.setOnClickListener {

            FirebaseAuth.getInstance().signOut()
            var intent: Intent
            intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)

        }

        btnChangePass.setOnClickListener {
            var intent = Intent(requireContext() , ChangePassActivity::class.java)
            startActivity(intent)

        }
//        imgMore.setOnClickListener {
//            var intent = Intent(activity,FavouriteRoomActivity::class.java)
//            startActivity(intent)
//        }
    }
    private fun setText(email:String?)
    {
        db= FirebaseFirestore.getInstance()
        if (email != null) {
            db.collection("USERS").document(email).get()
                .addOnSuccessListener {
                        tasks->
                    tvName.text=tasks.get("Name").toString()
                    tvPhone.text=tasks.get("Phone").toString()
                    tvEmail.text=tasks.get("email").toString()

                }
        }

    }

}