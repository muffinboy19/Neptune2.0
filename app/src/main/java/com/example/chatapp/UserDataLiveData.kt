package com.example.chatapp

import androidx.lifecycle.MutableLiveData

object UserDataLiveData {
    val imageUrlLiveData = MutableLiveData<String?>()
    val usernameLiveData = MutableLiveData<String?>()
    // Add other user data fields as MutableLiveData if needed
}
