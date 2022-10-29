package com.tuantd.myapplication.mainscreen.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tuantd.myapplication.R
import com.tuantd.myapplication.databinding.ActivityAddRoomBinding
import com.tuantd.myapplication.databinding.FragmentAccountBinding
import com.tuantd.myapplication.login.LoginActivity
import com.tuantd.myapplication.mainscreen.account.RoomFavourite.FavouriteRoomActivity
import com.tuantd.myapplication.mainscreen.account.aboutUs.AboutUsActivity
import com.tuantd.myapplication.mainscreen.account.myRoom.MyRoomActivity
import com.tuantd.myapplication.mainscreen.account.profile.ProfileActivity
import com.tuantd.myapplication.mainscreen.account.term.TermActivity


class AccountFragment : Fragment() {
    private lateinit var binding : FragmentAccountBinding

    lateinit var btnLogOut: Button
    lateinit var imgMore: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            var intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            (activity)?.finish()
        }

        binding.tvAboutUs.setOnClickListener {
            var intent = Intent(requireContext(),AboutUsActivity::class.java)
            startActivity(intent)
        }

        binding.tvProfile.setOnClickListener {
            var intent = Intent(requireContext(),ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.tvTerm.setOnClickListener {
            var intent = Intent(requireContext(),TermActivity::class.java)
            startActivity(intent)
        }

        binding.tvMyRoom.setOnClickListener {
            var intent = Intent(requireContext(),MyRoomActivity::class.java)
            startActivity(intent)
        }

        binding.tvMyFavRoom.setOnClickListener {
            var intent = Intent(requireContext(),FavouriteRoomActivity::class.java)
            startActivity(intent)
        }
    }


}