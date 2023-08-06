package com.example.chatapp

import android.net.Uri

class UserData private constructor() {

    var username: String? = null
    var userEmail: String? = null
    var imageUri: String? = null
    var imageUrl: String? = null
    var userId: String? = null

    companion object {
        @Volatile
        private var instance: UserData? = null

        fun getInstance(): UserData {
            return instance ?: synchronized(this) {
                instance ?: UserData().also { instance = it }
            }
        }
    }
}
