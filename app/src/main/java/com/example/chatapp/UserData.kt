package com.example.chatapp

class UserData private constructor() {

    var username: String? = null
    var userEmail: String? = null
    var imageUri: String? = null

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
