package com.tuantd.myapplication.mainscreen.posts

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.tuantd.myapplication.mainscreen.posts.DetailPost.DetailPostActivity
import com.tuantd.myapplication.R
import com.tuantd.myapplication.mainscreen.MainActivity
import com.tuantd.myapplication.mainscreen.home.Room


class PostsFragment : Fragment() {

    lateinit var content: Array<String>
    lateinit var rcv_post: RecyclerView
    private var loadDone:(() -> Unit)?=null
    var postList = ArrayList<Posts>()
    val postsAdapter = PostsAdapter()

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("post")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_posts, container, false)
        rcv_post = view.findViewById(R.id.rcv_posts)
        return view

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       // fetchData()

        loadDone={
            postsAdapter.addList(postList)
        }
        rcv_post.adapter = postsAdapter

        postsAdapter.onclickItem = {
            val intent =
                Intent((activity as MainActivity), DetailPostActivity::class.java)
            intent.putExtra("postId", it)
            (activity as MainActivity).startActivity(intent)
        }

        retrieveDataFromDatabase()


    }

//    private fun fetchData() {
//
//        FirebaseFirestore.getInstance().collection("POSTS")
//            .get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    post = documents.toObjects(Posts::class.java) as ArrayList<Posts>
//                }
//                postsAdapter.addList(post)
//
//
//            }
//            .addOnFailureListener {
//
//            }
//
//
//
//
//    }
private fun retrieveDataFromDatabase() {
    myReference.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            postList.clear()
            for (eachRoom in snapshot.children) {
                val room = eachRoom.value as? HashMap<*, *>
                val data = Posts(
                    id_bai_viet = room?.get("id_bai_viet") as String,
                    tieu_de = room["tieu_de"] as String,
                    noi_dung = room["noi_dung"] as String,
                    hinh_anh = room["hinh_anh"] as String
                )
                postList.add(data)
            }
            loadDone?.invoke()

        }

        override fun onCancelled(error: DatabaseError) {
        }
    })
}

}