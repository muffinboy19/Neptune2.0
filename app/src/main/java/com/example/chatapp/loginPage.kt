package com.example.chatapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.auth.User

/*
page 2
 */
class loginPage : AppCompatActivity() {

    private val RC_SIGN_IN = 9001
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var loadingView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        loadingView = findViewById<View>(R.id.loadingView)
        FirebaseApp.initializeApp(this)
//  code for the register page
        val registerLoginPage = findViewById<AppCompatButton>(R.id.registerLoginPage)
        registerLoginPage.setOnClickListener{


        }



//  code for google singin
        val  loginWithGoogle = findViewById<AppCompatButton>(R.id.loginWithGoogle)
        firebaseAuth = FirebaseAuth.getInstance()
        val imageViewGoogleSignIn: ImageView = findViewById(R.id.imageLoginWithGoogle)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        loginWithGoogle.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            startActivityForResult(signIntent,RC_SIGN_IN)
            showLoadingView()
        }

        imageViewGoogleSignIn.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            startActivityForResult(signIntent,RC_SIGN_IN)
            showLoadingView()
        }
//        imageViewGoogleSignIn.setOnClickListener {
//            val signIntent = googleSignInClient.signInIntent
//            startActivityForResult(signIntent,RC_SIGN_IN)
//        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    hideLoadingView()
                    val user = firebaseAuth.currentUser
                    val userEmail = user?.email
                    UserData.getInstance().userId = user?.uid
                    Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
                    // Intent for Homescreen activity
                    UserData.getInstance().userEmail = userEmail
                    // Intent for ProfileRegister activity
                    val profileRegisterIntent = Intent(this, profileRegister::class.java)
                    startActivity(profileRegisterIntent)
                    finish()
                } else {
                    Toast.makeText(this, "Sorry account was not created", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode== RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            handleSignInResult(task)
            try{
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            }
            catch (e:ApiException){
//                Toast.makeText(this,"Failed", Toast.LENGTH_SHORT).show()
                Log.w(TAG,"Google sign in failed",e)
            }
        }
    }
    private fun hideLoadingView() {
        loadingView.visibility = View.GONE
    }

    private fun showLoadingView() {
        loadingView.visibility = View.VISIBLE
    }
}