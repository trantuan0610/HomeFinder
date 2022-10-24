package com.tuantd.myapplication.mainscreen.posts.DetailPost

import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.firebase.database.*
import com.tuantd.myapplication.R

class DetailPostActivity : AppCompatActivity() {
    lateinit var webView: WebView
    lateinit var url: String

    lateinit var btnWifiOn: Button
    lateinit var btnWifiOff: Button
    lateinit var btnFreeOn: Button
    lateinit var btnFreeOff: Button
    lateinit var btnDieuHoaOn: Button
    lateinit var btnDieuHoaOff: Button
    val REQUEST_PERMISSION_CODE = 35
    val PICK_IMAGE_CODE = 3
    lateinit var viewPager: ViewPager
    lateinit var btnPickImage: Button

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("Test")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_post)

        viewPager = findViewById(R.id.viewPager)
        btnPickImage = findViewById(R.id.btnPickImage)


        btnPickImage.setOnClickListener {
1        }
    }
}

//        btnWifiOn = findViewById(R.id.wifi_on)
//        btnWifiOff = findViewById(R.id.wifi_off)
//        btnFreeOn = findViewById(R.id.free_on)
//        btnFreeOff = findViewById(R.id.free_off)
//        btnDieuHoaOn = findViewById(R.id.dieuhoa_on)
//        btnDieuHoaOff = findViewById(R.id.dieuhoa_off)
        //       retrieveDataFromDatabase("1")
// private fun retrieveDataFromDatabase(testID: String) {
//        myReference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                testList.clear()
//                for (eachRoom in snapshot.children) {
//                    val test = eachRoom.value as? HashMap<String, Any?>
//                    val data = Test(
//                        testID = test?.get("TestID") as String,
//                        xe = test["Xe"] as String,
//                        bep = test["bep"] as String,
//                        free = test["free"] as String,
//                        vesinh = test["vesinh"] as String,
//                        wifi = test["wifi"] as String
//
//
//                    )
//                    testList.add(data)
//                }
//                testList.forEach {
//                    if (it.testID.equals(testID)) {
//
//                        if (it.free == "1") {
//                            btnFreeOn.visibility = View.VISIBLE
//                            btnFreeOff.visibility = View.GONE
//                        }else{
//                            btnFreeOn.visibility = View.GONE
//                            btnFreeOff.visibility = View.VISIBLE
//                        }
//
//                        if (it.wifi == "1") {
//                            btnWifiOn.visibility = View.VISIBLE
//                            btnWifiOff.visibility = View.GONE
//                        }else{
//                            btnWifiOn.visibility = View.GONE
//                            btnWifiOff.visibility = View.VISIBLE
//                        }
//
//                        if (it.xe == "1") {
//                            btnDieuHoaOn.visibility = View.VISIBLE
//                            btnDieuHoaOff.visibility = View.GONE
//                        }else{
//                            btnDieuHoaOn.visibility = View.GONE
//                            btnDieuHoaOff.visibility = View.VISIBLE
//                        }
//
//                    }
//
//                }
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//            }
//        })
//    }

//        webView=  findViewById(R.id.webView)
//        intent.getStringExtra("url")?.let { webView.loadUrl(it) }

        //test image view








