package com.tuantd.myapplication.mainscreen.posts.DetailPost

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.tuantd.myapplication.databinding.ActivityDetailPostBinding
import com.tuantd.myapplication.mainscreen.MainActivity
import com.tuantd.myapplication.mainscreen.posts.Posts
import com.tuantd.myapplication.mainscreen.posts.PostsAdapter

class DetailPostActivity : AppCompatActivity() {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("post")
    lateinit var  binding: ActivityDetailPostBinding
    var postAdapter =  PostsAdapter()
    var listPost = ArrayList<Posts>()
    var listPostKhac = ArrayList<Posts>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var id = intent.getStringExtra("postId")
        if (id != null) {
            retrieveDataFromDatabase(id)
        }
        postAdapter.onclickItem = {
            val intent =
                Intent(this, DetailPostActivity::class.java)
                  intent.putExtra("postId", it)
                startActivity(intent)
        }

    }

    private fun retrieveDataFromDatabase(id: String) {
        myReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listPost.clear()
                listPostKhac.clear()
                for (eachRoom in snapshot.children) {
                    val room = eachRoom.value as? java.util.HashMap<*, *>
                    val data = Posts(
                        id_bai_viet = room?.get("id_bai_viet") as String,
                        tieu_de = room["tieu_de"] as String,
                        noi_dung = room["noi_dung"] as String,
                        hinh_anh = room["hinh_anh"] as String
                    )
                    listPost.add(data)
                }
                listPost.forEach {
                    if (it.id_bai_viet == id){
                        binding.tvTieude.text = it.tieu_de
                        binding.tvNoiDung.text = it.noi_dung
                        Glide.with(this@DetailPostActivity).load(it.hinh_anh).into(binding.imgAnh)
                    }
                }
                postAdapter.addList(listPost)
                binding.rcvPostKhac.adapter = postAdapter

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


}










