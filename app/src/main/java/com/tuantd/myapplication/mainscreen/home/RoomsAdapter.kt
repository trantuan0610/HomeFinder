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

open class RoomsAdapter(var context: Context, private var roomsList: ArrayList<Room>) : RecyclerView.Adapter<RoomsAdapter.MyViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.rooms_item, parent, false)
        return RoomsAdapter.MyViewHolder(itemView)
    }
    var onclickItem: ((String) -> Unit)? = null
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val room = roomsList[position]
        Glide.with(holder.itemView)
            .load(room.roomImage)
            .into(holder.roomImage)

        holder.price.text = room.price+ "triệu"
        holder.roomAddress.text = room.roomAddress
        holder.roomArea.text = room.roomArea + "m2"

        holder.roomImage.setOnClickListener {
            onclickItem?.invoke(room.roomId)
        }
    }

    override fun getItemCount(): Int = roomsList.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roomImage: ImageView = itemView.findViewById(R.id.imgRoom)
        val price: TextView = itemView.findViewById(R.id.tvRoomPrice)
        val roomAddress: TextView = itemView.findViewById(R.id.tvRoomAddress)
        val roomArea: TextView = itemView.findViewById(R.id.tvRoomArea)
    }
    fun addList(mRoomsList: ArrayList<Room>){
        roomsList=mRoomsList
        notifyDataSetChanged()
    }
}