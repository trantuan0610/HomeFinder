package com.tuantd.myapplication.mainscreen.posts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tuantd.myapplication.R

class PostsAdapter(private val postsList: ArrayList<Posts>) :
    RecyclerView.Adapter<PostsAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.posts_item, parent, false)
        return PostsAdapter.MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val post = postsList[position]
        holder.imagePost.setImageResource(post.postImageUrl)
        holder.title.text = post.postTitle
        holder.content.text = post.postContent

    }

    override fun getItemCount(): Int = postsList.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagePost: ImageView = itemView.findViewById(R.id.imgPost)
        val title: TextView = itemView.findViewById(R.id.tv_title)
        val content: TextView = itemView.findViewById(R.id.tv_content)
    }
}