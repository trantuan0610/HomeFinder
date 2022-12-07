package com.tuantd.myapplication.mainscreen

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tuantd.myapplication.R
import com.tuantd.myapplication.mainscreen.account.AccountFragment
import com.tuantd.myapplication.mainscreen.home.AddRoom.AddRoomActivity
import com.tuantd.myapplication.mainscreen.home.DetailRoom.DetailRoomActivity
import com.tuantd.myapplication.mainscreen.home.HomeFragment
import com.tuantd.myapplication.mainscreen.posts.DetailPost.DetailPostActivity
import com.tuantd.myapplication.mainscreen.posts.PostsFragment
import com.tuantd.myapplication.mainscreen.search.SearchFragment

class MainActivity : AppCompatActivity() {

    private var roomId = ""
    private var postId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (intent.extras?.isEmpty == false){
            intent.extras?.let {
                roomId = it.getString("roomId").toString()
                postId = it.getString("postId").toString()
            }
            if (roomId != ""){
                startActivity(Intent(this,
                    DetailRoomActivity::class.java).apply {
                    putExtra("roomId", roomId)
                } )
            }else if (postId != ""){
                startActivity(Intent(this,
                    DetailPostActivity::class.java).apply {
                    putExtra("postId", postId)
                } )
            }
        }


        val homeFragment = HomeFragment()
        val accountFragment = AccountFragment()
        val postFragment = PostsFragment()
        val searchFragment = SearchFragment()

        val bottomNavigationView =findViewById<BottomNavigationView>(R.id.buttonNavigation)
        setThatFragment(homeFragment)
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> setThatFragment(homeFragment)
                R.id.search -> setThatFragment(searchFragment)
                R.id.post -> setThatFragment(postFragment)
                R.id.account -> setThatFragment(accountFragment)
            }
            true
        }

    }
    private fun setThatFragment(fragment: Fragment) {
        //bắn sang accountFragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout,fragment)
        transaction.commit()
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}