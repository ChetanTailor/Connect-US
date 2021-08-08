package com.example.connectus

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latest_message_raw.view.*

class LatestMessageRaw(val gettinglatestM: ChatMessage): Item<ViewHolder>() {

    var chatpartneruser: User ?=null

    override fun bind(holder: ViewHolder, position: Int) {
        holder.itemView.recentMessage_textview_LMA.text = gettinglatestM.text

        val chatpartnerid: String
        if(gettinglatestM.fromID == FirebaseAuth.getInstance().uid){
            chatpartnerid = gettinglatestM.toID!!
        }
        else{
            chatpartnerid = gettinglatestM.fromID!!
        }

        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatpartnerid")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatpartneruser = snapshot.getValue(User::class.java)
                holder.itemView.username_textview_LMA.text = chatpartneruser?.username
                Picasso.get().load(chatpartneruser?.profileimageurl).into(holder.itemView.imageViewLMA)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
    override fun getLayout(): Int {
        return R.layout.latest_message_raw
    }

}