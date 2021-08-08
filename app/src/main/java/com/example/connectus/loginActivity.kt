package com.example.connectus

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class loginActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        LoginL.setOnClickListener{
           loginuser()
        }

        textviewgotoregisterL.setOnClickListener{
            finish()
        }

    }

    fun loginuser(){
        val email = emailL.text.toString()
        val password =  passwordL.text.toString()
        if ( email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All the inputs are required !", Toast.LENGTH_SHORT).show()
        }
        else {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)


                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this,latestMessagesActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "Login user Success", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(baseContext, "Login failed.", Toast.LENGTH_SHORT).show()
                    }
                }


        }
    }
}