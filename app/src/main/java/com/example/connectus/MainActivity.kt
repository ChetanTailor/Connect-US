package com.example.connectus

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RegisterR.setOnClickListener {
            performRegister()
        }

        textViewgotoL.setOnClickListener {
            val intent = Intent(this, loginActivity::class.java)
            startActivity(intent)
        }

        profile_image.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

    }

    var selectedphotouri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedphotouri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedphotouri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            profile_image.setBackgroundDrawable(bitmapDrawable)
        }
    }

    fun performRegister() {
        val username = usernameR.text.toString()
        val email = emailR.text.toString()
        val password = passwordR.text.toString()
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All the inputs are required !", Toast.LENGTH_SHORT).show()
        } else {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)

                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("TAG", "User successfully created email=$email")

                        uploadImagetoFirebaseStorage()

                    } else {
                        Log.d("TAG", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.", Toast.LENGTH_SHORT
                        ).show()
                    }
                }


        }
    }

    private fun uploadImagetoFirebaseStorage() {
        if (selectedphotouri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedphotouri!!)

            .addOnSuccessListener {
                Log.d("RAhai", "User successfully upladed image=${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("RAhai", "User successfully upladed filelocation=$it")
                    saveusertofirebasedatabase(it.toString())
                }

            }.addOnFailureListener() {

            }


    }

    private fun saveusertofirebasedatabase(profileimageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, usernameR.text.toString(), profileimageUrl)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("ok", "we saved the data")
                val intent = Intent(this, latestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }.addOnFailureListener {

            }
    }
}

