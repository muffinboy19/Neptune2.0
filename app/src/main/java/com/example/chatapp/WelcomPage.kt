package com.example.chatapp
/*
page 1
 */

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth

class WelcomPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcom_page)



        val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPrefs.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            val user = FirebaseAuth.getInstance().currentUser
            val userEmail = user?.email

            MyUserData.getInstance().userId = user?.uid
            MyUserData.getInstance().userEmail = userEmail

            // Intent for Homescreen activity
            val homeIntent = Intent(this, Homescreen::class.java)
            startActivity(homeIntent)
            finish()
            return
        }
        val agreeAndContinueButton = findViewById<AppCompatButton>(R.id.agreeAndContinueButton)
        agreeAndContinueButton.setOnClickListener {
            val intent = Intent(this, loginPage::class.java)
            startActivity(intent)
            finish()
        }

    }
}