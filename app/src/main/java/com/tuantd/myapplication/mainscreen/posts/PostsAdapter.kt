package com.tuantd.myapplication.mainscreen.posts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tuantd.myapplication.R
import com.tuantd.myapplication.mainscreen.MainActivity
import com.tuantd.myapplication.mainscreen.home.Room

class PostsAdapter() : RecyclerView.Adapter<PostsAdapter.MyViewHolder>() {

    private var postsList: ArrayList<Posts>?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.posts_item, parent, false)
        return PostsAdapter.MyViewHolder(itemView)
    }

    var onclickItem: ((String) -> Unit)? = null

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val post = postsList?.get(position)
        holder.title.text = post?.tieu_de
        holder.content.text = post?.noi_dung
        //Images with Glide
        Glide.with(holder.itemView)
            .load(post?.hinh_anh)
            .into(holder.imagePost)

        holder.imagePost.setOnClickListener {
            post?.id_bai_viet?.let { it1->
                onclickItem?.invoke(it1)
            }
        }

    }

    override fun getItemCount(): Int = postsList?.size?:0

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagePost: ImageView = itemView.findViewById(R.id.imgPost)
        val title: TextView = itemView.findViewById(R.id.tv_title)
        val content: TextView = itemView.findViewById(R.id.tv_content)
    }
    fun addList(array: ArrayList<Posts>){
        postsList=array
        notifyDataSetChanged()
    }
}