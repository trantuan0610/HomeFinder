package com.tuantd.myapplication.mainscreen.posts

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tuantd.myapplication.DetailPost.DetailPostActivity
import com.tuantd.myapplication.R
import com.tuantd.myapplication.mainscreen.MainActivity

class PostsAdapter(private val context: Context, private val postsList: List<Posts>) :
    RecyclerView.Adapter<PostsAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.posts_item, parent, false)
        return PostsAdapter.MyViewHolder(itemView)
    }

    var onclickItem: ((String) -> Unit)? = null

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val post = postsList[position]
        val activity = MainActivity()
        holder.title.text = post.postTitle
        holder.content.text = post.postContent
        //Images with Glide
        Glide.with(context)
            .load(post.postImageUrl)
            .into(holder.imagePost)

        holder.imagePost.setOnClickListener {
            onclickItem?.invoke(post.url)
        }

    }

    override fun getItemCount(): Int = postsList.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagePost: ImageView = itemView.findViewById(R.id.imgPost)
        val title: TextView = itemView.findViewById(R.id.tv_title)
        val content: TextView = itemView.findViewById(R.id.tv_content)
    }
}