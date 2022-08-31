package com.tuantd.myapplication.mainscreen.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tuantd.myapplication.R

class RoomsAdapter(var context: Context, private val roomsList: ArrayList<Room>) : RecyclerView.Adapter<RoomsAdapter.MyViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.rooms_item, parent, false)
        return RoomsAdapter.MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val room = roomsList[position]
        Glide.with(context)
            .load(room.roomImage)
            .into(holder.roomImage)

        holder.price.text = room.price
        holder.roomAddress.text = room.roomAddress
        holder.roomArea.text = room.roomArea
    }

    override fun getItemCount(): Int = roomsList.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roomImage: ImageView = itemView.findViewById(R.id.imgRoom)
        val price: TextView = itemView.findViewById(R.id.tvRoomPrice)
        val roomAddress: TextView = itemView.findViewById(R.id.tvRoomAddress)
        val roomArea: TextView = itemView.findViewById(R.id.tvRoomArea)
    }
}