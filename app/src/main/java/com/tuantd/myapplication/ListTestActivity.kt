package com.tuantd.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.tuantd.myapplication.databinding.ActivityListTestBinding
import com.tuantd.myapplication.mainscreen.MainActivity
import com.tuantd.myapplication.mainscreen.home.Room
import com.tuantd.myapplication.mainscreen.home.RoomsAdapter

class ListTestActivity : AppCompatActivity() {
    lateinit var binding: ActivityListTestBinding
    private var loadDone:(() -> Unit)?=null

    private var testList = ArrayList<Test>()
    private val testAdapter= TestAdapter()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("Test")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListTestBinding.inflate(layoutInflater)

        setContentView(binding.root)
        retrieveDataFromDatabase()
        loadDone={
            testAdapter.addList(testList)
        }
        binding.rcvTest.adapter = testAdapter

        testAdapter.onclickItem ={
            val intent = Intent(this,DetailTestActivity::class.java)
            intent.putExtra("testId", it)
            startActivity(intent)
        }
    }

    private fun retrieveDataFromDatabase() {
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
                    testList.add(data)
                }
                loadDone?.invoke()

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}