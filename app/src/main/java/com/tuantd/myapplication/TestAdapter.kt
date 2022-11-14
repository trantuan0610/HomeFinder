package com.tuantd.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tuantd.myapplication.mainscreen.home.Room
import com.tuantd.myapplication.mainscreen.home.RoomsAdapter

class TestAdapter : RecyclerView.Adapter<TestAdapter.MyViewHolder>() {
    private var testList: ArrayList<Test>?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.test_item, parent, false)
        return TestAdapter.MyViewHolder(itemView)
    }
    var onclickItem: ((String) -> Unit)? = null
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val test = testList?.get(position)
        Glide.with(holder.itemView)
            .load(test?.listImage?.get(0))
            .into(holder.testImage)

        holder.testAddress.text = "test?.roomAddress"
        holder.testArea.text =  "test?.roomArea"
        holder.testName.text =  "test?.nameArea"

        holder.testImage.setOnClickListener {
            test?.testID?.let { it1 -> onclickItem?.invoke(it1) }
        }
    }

    override fun getItemCount(): Int = testList?.size?:0

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val testImage: ImageView = itemView.findViewById(R.id.imgTest)
        val testAddress: TextView = itemView.findViewById(R.id.tvTestPrice)
        val testArea: TextView = itemView.findViewById(R.id.tvTestAddress)
        val testName: TextView = itemView.findViewById(R.id.tvTestArea)
    }
    fun addList(mTestList: ArrayList<Test>){
        testList=mTestList
        notifyDataSetChanged()
    }
}