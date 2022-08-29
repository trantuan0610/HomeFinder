package com.tuantd.myapplication.mainscreen.posts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tuantd.myapplication.R


class PostsFragment : Fragment() {
    private lateinit var newArrayList: ArrayList<Posts>
    lateinit var imageID : Array<Int>
    lateinit var title : Array<String>
    lateinit var content : Array<String>
    lateinit var rcv_post : RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_posts, container, false)
        rcv_post = view.findViewById(R.id.rcv_posts)
        return view

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageID = arrayOf(R.drawable.imgdaidien
            ,R.drawable.imgdaidien
            ,R.drawable.imgdaidien
            ,R.drawable.imgdaidien
            ,R.drawable.imgdaidien
            ,R.drawable.imgdaidien
            ,R.drawable.imgdaidien)
        title = arrayOf("Tuấn Trần"
            ,"Tuấn Trần"
            ,"Tuấn Trần"
            ,"Tuấn Trần"
            ,"Tuấn Trần"
            ,"Tuấn Trần"
            ,"Tuấn Trần")
        content = arrayOf("Tuấn Trần"
            ,"Tuấn Trần"
            ,"Tuấn Trần"
            ,"Tuấn Trần"
            ,"Tuấn Trần"
            ,"Tuấn Trần"
            ,"Tuấn Trần")

        newArrayList = arrayListOf<Posts>()
        getUserdata()

    }

    private fun getUserdata() {
        for(i in imageID.indices){
            val posts = Posts(i,title[i],imageID[i],content[i])
            newArrayList.add(posts)
        }

        rcv_post.adapter = PostsAdapter(newArrayList)

    }

}