package com.tuantd.myapplication.mainscreen.account.RoomFavourite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tuantd.myapplication.R
import com.tuantd.myapplication.mainscreen.home.DetailRoom.FavouriteRoom

open class FavouriteRoomAdapter() : RecyclerView.Adapter<FavouriteRoomAdapter.MyViewHolder>() {
    private var roomsFavList: ArrayList<FavouriteRoom>?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.rooms_item, parent, false)
        return FavouriteRoomAdapter.MyViewHolder(itemView)
    }
    var onclickItem: ((String) -> Unit)? = null
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val roomFav = roomsFavList?.get(position)
        Glide.with(holder.itemView)
            .load(roomFav?.roomImageFav)
            .into(holder.roomImage)

        holder.price.text = "${roomFav?.priceFav} triệu"
        holder.roomAddress.text = roomFav?.roomAddressFav
        holder.roomArea.text = roomFav?.roomAreaFav + "m2"

        holder.roomImage.setOnClickListener {
            roomFav?.favouriteRoomID?.let { it1 -> onclickItem?.invoke(it1) }
        }
    }

    override fun getItemCount(): Int = roomsFavList?.size?:0
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        val roomImage: ImageView = itemView.findViewById(R.id.imgRoom)
        val price: TextView = itemView.findViewById(R.id.tvRoomPrice)
        val roomAddress: TextView = itemView.findViewById(R.id.tvRoomAddress)
        val roomArea: TextView = itemView.findViewById(R.id.tvRoomArea)
    }
    fun addList(mRoomsList: ArrayList<FavouriteRoom>){
        roomsFavList=mRoomsList
        notifyDataSetChanged()
    }

}