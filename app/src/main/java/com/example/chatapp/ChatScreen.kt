package com.example.chatapp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class ChatScreen : AppCompatActivity() {
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageBox :EditText
    private lateinit var sendButton :ImageView
    private lateinit var messageAdapter: messageAdapter
    private lateinit var messageList : ArrayList<messaage>
    private lateinit var mdbref : DatabaseReference

    var recciverRoom: String? = null
    var senderRoom : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatscreen)
        val Toolba2 = findViewById<TextView>(R.id.toolbar2)
        val name = intent.getStringExtra("name")
        val Recciveruid = intent.getStringExtra("uid")
        mdbref = FirebaseDatabase.getInstance().getReference()

        val senderUId  = FirebaseAuth.getInstance().currentUser?.uid
        senderRoom= Recciveruid + senderUId
        recciverRoom = senderUId + Recciveruid
        Toolba2.text = name




        messageList = ArrayList()
        messageAdapter = messageAdapter(this,messageList)
        messageRecyclerView = findViewById(R.id.chatt)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.send3)



        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageAdapter
        mdbref.child("chats").child(senderRoom!!).child("message")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for(postSnap in snapshot.children){
                        val messaage = postSnap.getValue(messaage::class.java)
                        messageList.add(messaage!!)
                        messageAdapter.notifyDataSetChanged()
                        messageRecyclerView.smoothScrollToPosition(messageAdapter.itemCount - 1)
                        messageRecyclerView.postDelayed({
                            messageRecyclerView.smoothScrollToPosition(messageAdapter.itemCount - 1)
                        }, 200) // Delay in milliseconds (adjust as needed)


                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }


            })
        sendButton.setOnClickListener{





            // here the messaeg is addeed to the data base
            val message : String? = messageBox.text.toString()
            val messageObject =  messaage(message,senderUId)
            mdbref.child("chats").child(senderRoom!!).child("message").push()
                .setValue(messageObject).addOnSuccessListener {

                }
            mdbref.child("chats").child(recciverRoom!!).child("message").push()
                .setValue(messageObject).addOnSuccessListener {

                }

            messageAdapter.notifyDataSetChanged()
            messageBox.setText("")

        }











    }
}