package com.example.connectus

class ChatMessage(val id: String?, val text: String, val fromID: String?, val toID:String?, val timestaMp: Long ){
    constructor() : this("","","","",-1)
}