package com.tuantd.myapplication.mainscreen.posts.DetailPost

import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tuantd.myapplication.R
import com.tuantd.myapplication.mainscreen.home.Room

class DetailPostActivity : AppCompatActivity() {
    lateinit var webView: WebView
    lateinit var url: String
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("Test")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_post)


//        webView=  findViewById(R.id.webView)
//        intent.getStringExtra("url")?.let { webView.loadUrl(it) }

        addRoomToDatabase()

    }

    private fun addRoomToDatabase() {
        val id: String = myReference.push().key.toString()

        val test = Test(
            id,
            "Test",
            Room("1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1")
        )

        myReference.child(id).setValue(test).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                finish()
            } else {
//                Toast.makeText(
//                    this,
//                    task.exception.toString(),
//                    Toast.LENGTH_LONG
//                ).show()

            }
        }
    }

}










