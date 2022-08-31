package com.tuantd.myapplication.mainscreen.posts

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.tuantd.myapplication.DetailPost.DetailPostActivity
import com.tuantd.myapplication.R
import com.tuantd.myapplication.mainscreen.MainActivity


class PostsFragment : Fragment() {
    private lateinit var newArrayList: ArrayList<Posts>
    lateinit var imageID: Array<Int>
    lateinit var title: Array<String>
    lateinit var content: Array<String>
    lateinit var rcv_post: RecyclerView

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

        fetchData()

       // newArrayList = arrayListOf<Posts>()


    }

    private fun fetchData() {

        FirebaseFirestore.getInstance().collection("POSTS")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val post = documents.toObjects(Posts::class.java)

                    val posts = PostsAdapter(requireContext(), post)
                    rcv_post.adapter = posts
                    posts.onclickItem = {
                        val intent =
                            Intent((activity as MainActivity), DetailPostActivity::class.java)
                        intent.putExtra("url", it)
                        (activity as MainActivity).startActivity(intent)
                    }
                }

            }
            .addOnFailureListener {

            }

    }



}