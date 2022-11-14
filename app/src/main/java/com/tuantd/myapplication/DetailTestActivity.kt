package com.tuantd.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.database.*
import com.tuantd.myapplication.databinding.ActivityDetailTestBinding

class DetailTestActivity : AppCompatActivity() {
    lateinit var binding : ActivityDetailTestBinding
    private var testList = ArrayList<Test>()
    private var testDetail: Test? = null
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference : DatabaseReference = database.reference.child("Test")
    val imageList = ArrayList<SlideModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailTestBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val testId = intent.getStringExtra("testId")
        if (testId != null) {
            getData(testId)
        }
         binding.imageSlider.setImageList(imageList,ScaleTypes.FIT)

    }

    private fun getData(testId: String) {
        myReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                testList.clear()
                for (eachtest in snapshot.children) {
                    val test = eachtest.value as? HashMap<*, *>
                    val data = Test(
                        testID = test?.get("testID") as String,
                        testAdd = test["testAdd"] as String,
                        testName = test["testName"] as String,
                        listImage = test["listImage"] as List<String>
                    )
                    Log.d("TUANAAA",data.toString())
                    testList.add(data)
                }
                testList.forEach {
                    if (it.testID == testId){
                        testDetail = it
                    }
                }
                for (i in testDetail?.listImage!!) {
                    imageList.add(SlideModel(i))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DetailTestActivity,"ko duoc",Toast.LENGTH_SHORT).show()
            }
        })
    }

}