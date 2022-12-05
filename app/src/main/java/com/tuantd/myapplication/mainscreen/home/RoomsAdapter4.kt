package com.tuantd.myapplication.mainscreen.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tuantd.myapplication.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

open class RoomsAdapter4() : RecyclerView.Adapter<RoomsAdapter4.MyViewHolder>() {



    private var roomsList: ArrayList<Room>?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.rooms_item3, parent, false)
        return RoomsAdapter4.MyViewHolder(itemView)
    }
    var onclickItem: ((String) -> Unit)? = null

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val room = roomsList?.get(position)
        Glide.with(holder.itemView)
            .load(room?.list_image?.get(0))
            .into(holder.roomImage)
        //
        val symbols = DecimalFormatSymbols()
        symbols.setDecimalSeparator(',')
        val decimalFormat = DecimalFormat("###,###,###,###Đ", symbols)
        holder.price.setText(decimalFormat.format(room?.gia?.toInt() ?: 0))
        //
//        holder.price.text = "${room?.gia} Đ"
        holder.roomAddress.text = room?.dia_chi
        holder.roomArea.text = room?.dien_tich + "m2"
        holder.roomDate.text = room?.thoi_gian
        holder.roomTitle.text = room?.tieu_de
        holder.roomImage.setOnClickListener {
            room?.id_bai_dang?.let { it1 -> onclickItem?.invoke(it1) }
        }
    }

    override fun getItemCount(): Int = roomsList?.size?:0

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roomImage: ImageView = itemView.findViewById(R.id.imgRoom)
        val price: TextView = itemView.findViewById(R.id.tvRoomPrice)
        val roomAddress: TextView = itemView.findViewById(R.id.tvRoomAddress)
        val roomArea: TextView = itemView.findViewById(R.id.tvRoomArea)
        val roomDate: TextView = itemView.findViewById(R.id.tvRoomDate)
        val roomTitle: TextView = itemView.findViewById(R.id.tvRoomTitle)
    }
    fun addList(mRoomsList: ArrayList<Room>){
        roomsList=mRoomsList
        notifyDataSetChanged()
    }
}