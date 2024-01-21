package com.example.quiztopia.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.quiztopia.R
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

class LoginIntro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_intro)
        val auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null) {
            Toast.makeText(this, "User is logged in!", Toast.LENGTH_SHORT).show()
            redirect("MAIN")
        }

        findViewById<Button>(R.id.btnGetStarted).setOnClickListener {
            redirect("LOGIN")
        }
    }

    private fun redirect(name:String) {
        val intent = when(name) {
            "LOGIN" -> Intent(this, LoginActivity::class.java)
            "MAIN" -> Intent(this, MainActivity::class.java)
            else -> throw Exception("No Path Exists")
        }
        startActivity(intent)
        finish()
    }
}