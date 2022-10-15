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
import com.tuantd.myapplication.mainscreen.home.HomeFragment
import com.tuantd.myapplication.mainscreen.posts.PostsFragment
import com.tuantd.myapplication.mainscreen.search.SearchFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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
        if (fragment != null){
            //bắn sang accountFragment
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout,fragment)
            transaction.commit()
        }
    }
}