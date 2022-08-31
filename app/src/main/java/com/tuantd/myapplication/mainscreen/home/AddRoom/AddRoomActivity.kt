package com.tuantd.myapplication.mainscreen.home.AddRoom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tuantd.myapplication.R
import com.tuantd.myapplication.mainscreen.home.Room

class AddRoomActivity : AppCompatActivity() {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference: DatabaseReference = database.reference.child("Rooms")

    lateinit var btnAdd: Button
    lateinit var edtPrice: EditText
    lateinit var edtDes: EditText
    lateinit var edtArea: EditText
    lateinit var edtAddress: EditText
    lateinit var edtImage: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_room)

        btnAdd = findViewById(R.id.btnAddRoom)
        edtPrice = findViewById(R.id.edtPrice)
        edtDes = findViewById(R.id.edtDes)
        edtArea = findViewById(R.id.edtArea)
        edtAddress = findViewById(R.id.edtRoomAddress)
        edtImage = findViewById(R.id.edtImage)





        btnAdd.setOnClickListener {
            addRoomToDatabase()
        }
    }

    private fun addRoomToDatabase() {
        val address: String = edtAddress.text.toString()
        val area: String = edtArea.text.toString()
        val price: String = edtPrice.text.toString()
        val image: String = edtImage.text.toString()
        val des: String = edtDes.text.toString()

        val id: String = myReference.push().key.toString()

        val room = Room(id, address, image, price, area, des)

        myReference.child(id).setValue(room).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this,"The new user has been added to the database",Toast.LENGTH_LONG).show()
                finish()

            } else {
                Toast.makeText(
                    this,
                    task.exception.toString(),
                    Toast.LENGTH_LONG
                ).show()

            }
        }
    }
}