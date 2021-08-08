package com.example.connectus


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_messages.*
import kotlinx.android.synthetic.main.latest_message_raw.view.*

class latestMessagesActivity : AppCompatActivity() {


    companion object{
        var currintUser: User? = null
    }


    val adapter = GroupAdapter<ViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)

        verifyuser()
        listenFORlatestMessage()
        fetchCurrentUser()
        recyclerView_for_latestMA.adapter = adapter
        recyclerView_for_latestMA.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        adapter.setOnItemClickListener{item , view->
            val intent = Intent(this,ChatActivity::class.java)
            val raw = item as LatestMessageRaw
            intent.putExtra(new_messages_activity.USER_KEY,raw.chatpartneruser)//clicking on any raw leads to chat activity of the perticular person
            startActivity(intent)
        }
    }

    val latestMessageMap = HashMap<String, ChatMessage>()
    private fun refreshLatestmessageview(){
        adapter.clear()
        latestMessageMap.values.forEach({
            adapter.add(LatestMessageRaw(it))
        })
    }

    fun listenFORlatestMessage(){
        val fromID = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest MessaGes/$fromID")
        ref.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val latestmessage = snapshot.getValue(ChatMessage::class.java)
                if(latestmessage!=null) {
                    latestMessageMap[snapshot.key!!] = latestmessage
                    refreshLatestmessageview()
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val latestmessage = snapshot.getValue(ChatMessage::class.java)
                if(latestmessage!=null) {
                    latestMessageMap[snapshot.key!!] = latestmessage
                    refreshLatestmessageview()
                }
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })




    }

    //function was created for fetchig the current logged in user imageurl for chet activity
    fun fetchCurrentUser(){
        val uid = FirebaseAuth.getInstance().uid
        val ref  = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                currintUser = snapshot.getValue(User::class.java)
                // Log.d("Username","${currintUser?.profileimageurl}")
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun verifyuser(){
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null){
            val intent = Intent(this , MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nevi_menu , menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId ) {

            R.id.Menu_newMessagesid -> {
                val intent = Intent(this, new_messages_activity::class.java)
                startActivity(intent)
            }

            R.id.menu_signoutid -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this , MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

        }

        return super.onOptionsItemSelected(item)
    }


}