
package com.example.chatapp
/*
this page would contain regitser and lgoin links
 */

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton

class WelcomPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcom_page)

        val agreeAndContinueButton = findViewById<AppCompatButton>(R.id.agreeAndContinueButton)

    }
}