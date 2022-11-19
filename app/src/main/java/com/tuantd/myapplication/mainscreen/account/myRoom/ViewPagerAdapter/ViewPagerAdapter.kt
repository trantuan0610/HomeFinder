package com.tuantd.myapplication.mainscreen.account.myRoom.ViewPagerAdapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tuantd.myapplication.mainscreen.account.myRoom.fragment.MyListRoomHideFragment
import com.tuantd.myapplication.mainscreen.account.myRoom.fragment.MyListRoomShowFragment

class ViewPagerAdapter (activity: FragmentActivity, private val tabCount: Int):
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = tabCount

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                MyListRoomShowFragment()
            }
            1 -> {
                MyListRoomHideFragment()
            }
            else -> {
                MyListRoomShowFragment()
            }


        }
    }
}