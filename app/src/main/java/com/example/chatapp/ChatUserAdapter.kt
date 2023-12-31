package com.example.chatapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ChatUserAdapter(val context: Context, val chatuserList: ArrayList<ChatUser>) :
    RecyclerView.Adapter<ChatUserAdapter.ChatUserViewHolder>() {

    class ChatUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameUser: TextView = itemView.findViewById(R.id.name)
        val userDp: ImageView = itemView.findViewById(R.id.dp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatUserViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.chatuser_layout, parent, false)
        return ChatUserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chatuserList.size
    }

    override fun onBindViewHolder(holder: ChatUserViewHolder, position: Int) {
        val currentUser = chatuserList[position]
        holder.nameUser.text = currentUser.username
        Picasso.get().load(currentUser.userImageUrl).into(holder.userDp)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatScreen::class.java)
            intent.putExtra("name", currentUser.username)
            intent.putExtra("imageUrl", currentUser.userImageUrl)
            intent.putExtra("uid", currentUser.UserId) // Add the UID to the intent
            context.startActivity(intent)
        }
    }
}
