package com.example.chatapp

class MyUserData private constructor() {

    var username: String? = null
    var userEmail: String? = null
    var imageUrl: String? = null
    var userId: String? = null


    companion object {
        @Volatile
        private var instance: MyUserData? = null

        fun getInstance(): MyUserData {
            return instance ?: synchronized(this) {
                instance ?: MyUserData().also { instance = it }
            }
        }
    }
}
