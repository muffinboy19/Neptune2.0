package com.example.chatapp

class messaage {


    var messaage: String? = null
    var senderId : String? = null
    constructor(){}
    constructor(messaage: String?,senderId:String?){
        this.messaage=messaage
        this.senderId =senderId
    }
}