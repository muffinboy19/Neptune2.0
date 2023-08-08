package com.example.chatapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth

class messageAdapter(val context: Context, val messageList: ArrayList<messaage>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    val ITEM_Reccived = 1
    val ITEM_sent = 2


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        if (viewType == 1) {
            val view = LayoutInflater.from(context).inflate(R.layout.reccive_message, parent, false)
            return ReciveViewHolder(view)

        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.send_message, parent, false)
            return SentViewHolder(view)
        }

    }

    override fun getItemViewType(position: Int): Int {
        val cuurentMessage = messageList[position]
        if (FirebaseAuth.getInstance().currentUser?.uid == cuurentMessage.senderId) {
            return ITEM_sent
        } else {
            return ITEM_Reccived
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMessge = messageList[position]
        if (holder.javaClass == SentViewHolder::class.java) {
            val ViewHolder = holder as SentViewHolder
            holder.Sentmessage.text = currentMessge.messaage

        } else {
            // here the reccive Viewholder thing comes
            val ViewHolder = holder as ReciveViewHolder
            holder.reccivetmessage.text = currentMessge.messaage
        }
    }


    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Sentmessage = itemView.findViewById<TextView>(R.id.textsent)


    }

    class ReciveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reccivetmessage = itemView.findViewById<TextView>(
            R.id.textreccived
        )

    }


}

