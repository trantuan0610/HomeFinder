package com.tuantd.myapplication.mainscreen.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tuantd.myapplication.R
import java.io.InputStream
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.res.Resources;
import android.widget.GridView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tuantd.myapplication.mainscreen.posts.Posts
import com.tuantd.myapplication.mainscreen.posts.PostsAdapter
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.nio.file.attribute.AclEntry
import java.util.Scanner;

class HomeFragment : Fragment() {

    private lateinit var newArrayList: ArrayList<Room>
    lateinit var roomImage: Array<Int>
    lateinit var roomAddress: Array<String>
    lateinit var roomPrice: Array<String>
    lateinit var roomArea: Array<String>
    lateinit var rcv_room: RecyclerView
    lateinit var viewItems: List<Object>
    lateinit var tv1: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        rcv_room = view.findViewById(R.id.rcv_room)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv1 = view.findViewById(R.id.tv1)



        roomImage = arrayOf(
            R.drawable.room1,
            R.drawable.room1,
            R.drawable.room1,
            R.drawable.room1,
            R.drawable.room1,
            R.drawable.room1,
            R.drawable.room1
        )

        roomAddress = arrayOf(
            "Tuấn Trần",
            "Tuấn Trần",
            "Tuấn Trần",
            "Tuấn Trần",
            "Tuấn Trần",
            "Tuấn Trần",
            "Tuấn Trần"
        )

        roomPrice = arrayOf(
            "Tuấn Trần",
            "Tuấn Trần",
            "Tuấn Trần",
            "Tuấn Trần",
            "Tuấn Trần",
            "Tuấn Trần",
            "Tuấn Trần"
        )

        roomArea = arrayOf(
            "Tuấn Trần",
            "Tuấn Trần",
            "Tuấn Trần",
            "Tuấn Trần",
            "Tuấn Trần",
            "Tuấn Trần",
            "Tuấn Trần"
        )


        newArrayList = arrayListOf<Room>()
        getRoomdata()

    }

    private fun getRoomdata() {
        for (i in roomImage.indices) {
            val room = Room(i, roomAddress[i], roomImage[i], roomPrice[i], roomArea[i])
            newArrayList.add(room)
        }
        rcv_room.adapter = RoomsAdapter(newArrayList)
    }




}



