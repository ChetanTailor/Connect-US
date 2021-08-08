package com.example.connectus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.chat_from_raw.view.*
import kotlinx.android.synthetic.main.chat_to_raw_right.view.*
import java.net.URL

class ChatActivity : AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val user = intent.getParcelableExtra<User>(new_messages_activity.USER_KEY)
        if (user != null) {
            supportActionBar?.title = user.username
        } else {
            supportActionBar?.title = "User"
        }

        listenformessages()

        sendbutton_chat.setOnClickListener() {
            preformsendmessage()
            recyclerview_chat.scrollToPosition(adapter.itemCount-1)
        }

        recyclerview_chat.adapter = adapter

    }

    private fun preformsendmessage() {
        val Mtext = edittext_chatmassage.text.toString()
        val fromID = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(new_messages_activity.USER_KEY)
        val toID = user?.uid


        // val reference = FirebaseDatabase.getInstance().getReference("/MessAges").push()
        val reference =
            FirebaseDatabase.getInstance().getReference("/user-to-user_MessAges/$fromID/$toID")
                .push()
        val chatMessage =
            ChatMessage(reference.key, Mtext, fromID, toID, System.currentTimeMillis() / 1000)

        //for user to user message listening
        val torefrence =
            FirebaseDatabase.getInstance().getReference("/user-to-user_MessAges/$toID/$fromID")
                .push()


        val latestMessageREF = FirebaseDatabase.getInstance().getReference("/latest MessaGes/$fromID/$toID")
        val TOlatestMessageREF = FirebaseDatabase.getInstance().getReference("/latest MessaGes/$toID/$fromID")

        reference.setValue(chatMessage)
        torefrence.setValue(chatMessage)

        latestMessageREF.setValue(chatMessage)
        TOlatestMessageREF.setValue(chatMessage)


    }

    private fun listenformessages() {
        val fromID = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(new_messages_activity.USER_KEY)
        val toID = user?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-to-user_MessAges/$fromID/$toID")

        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                val chatMessage = snapshot.getValue(ChatMessage::class.java)


                if (chatMessage?.text != null) {
                    val user = intent.getParcelableExtra<User>(new_messages_activity.USER_KEY)
                    if (chatMessage.fromID == FirebaseAuth.getInstance().uid) {
                        var currentuser = latestMessagesActivity.currintUser
                        if (currentuser != null) {
                            adapter.add(ChatToItemsRS(chatMessage.text,currentuser.profileimageurl))
                        }
                    } else {
                        if (user != null) {
                            adapter.add(ChatFromItemsLS(chatMessage.text, user.profileimageurl))
                        }
                    }
                    edittext_chatmassage.setText("")
                }
                recyclerview_chat.scrollToPosition(adapter.itemCount-1)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }
            override fun onCancelled(error: DatabaseError) {

            }

        }
        )

    }

}

class ChatFromItemsLS(val text: String, val PIurlL: String) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.message_CA_INCOMING.text = text
        Picasso.get().load(PIurlL).into(viewHolder.itemView.imageView_CAL)

    }

    override fun getLayout(): Int {
        return R.layout.chat_from_raw
    }
}

class ChatToItemsRS(val text: String, val imgurl: String) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.message_CA_OUTGOING.text = text
        Picasso.get().load(imgurl).into(viewHolder.itemView.imageView_CAR)

    }

    override fun getLayout(): Int {
        return R.layout.chat_to_raw_right
    }
}

