
package com.example.chatapp
/*
page 1
 */

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton

class WelcomPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcom_page)

        val agreeAndContinueButton = findViewById<AppCompatButton>(R.id.agreeAndContinueButton)
        agreeAndContinueButton.setOnClickListener{
            val intent = Intent(this,loginPage::class.java)
            startActivity(intent)
            finish()
        }

    }
}