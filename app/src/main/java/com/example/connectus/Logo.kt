package com.example.connectus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_logo.*

class Logo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logo)


        imageView.animate().setDuration(2500).alpha(1f).withEndAction{
            val intent = Intent(this, latestMessagesActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.slide_in_left , android.R.anim.slide_out_right)
            finish()
        }
    }
}

