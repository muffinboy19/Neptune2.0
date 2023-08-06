package com.example.chatapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID


class Homescreen : AppCompatActivity() {
    private val db  = FirebaseFirestore.getInstance()
    private lateinit var auth:FirebaseAuth
       private lateinit var rc: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var mdref :DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        FirebaseApp.initializeApp(this)
        val username = UserData.getInstance().username
        val userEmail = UserData.getInstance().userEmail
        val userId = UserData.getInstance().userId


    }








}