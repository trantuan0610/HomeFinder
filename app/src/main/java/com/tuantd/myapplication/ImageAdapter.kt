package com.tuantd.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    private var imageList : ArrayList<String>?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        return ImageAdapter.ImageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = imageList?.get(position)
       Glide.with(holder.itemView).load(image).into(holder.image)
    }

    override fun getItemCount(): Int = imageList?.size?:0

    class ImageViewHolder (itemView : View): RecyclerView.ViewHolder(itemView) {
        val image : ImageView = itemView.findViewById(R.id.itemImage)

    }
    fun addList(mImageList: ArrayList<String>){
        imageList=mImageList
        notifyDataSetChanged()
    }
}