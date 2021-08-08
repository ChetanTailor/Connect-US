package com.example.connectus

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_new_messages.*
import kotlinx.android.synthetic.main.user_raw_newmessage.view.*


class new_messages_activity : AppCompatActivity() {




    companion object{
        val USER_KEY = "USER_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_messages)

        supportActionBar?.title = "Select User"
        fetchusers()

    }


    private fun fetchusers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val adapter = GroupAdapter<ViewHolder>()
                snapshot.children.forEach {
                    Log.d("working", it.toString())
                    val user = it.getValue(User::class.java)
                    if (user != null) {
                        adapter.add(useritemsclass(user))
                    }
                }
                adapter.setOnItemClickListener { item, view ->
                    val a = item as useritemsclass
                    val intent = Intent(view.context,ChatActivity::class.java)
                    intent.putExtra(USER_KEY , a.user ) // taking whole userdata to other class
                    startActivity(intent)
                    finish()
                }

                rvNewMID.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        )
    }

}

class useritemsclass(val user: User) : Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username_TV_NM.text = user.username
        Picasso.get().load(user.profileimageurl).into(viewHolder.itemView.imageView_NM)
    }

    override fun getLayout(): Int {
        return R.layout.user_raw_newmessage
    }

}


