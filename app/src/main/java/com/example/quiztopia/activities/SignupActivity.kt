package com.example.quiztopia.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import com.example.quiztopia.R
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        firebaseAuth = FirebaseAuth.getInstance()
        findViewById<Button>(R.id.btnSignUp).setOnClickListener() {
            signUpUser()
        }

        findViewById<TextView>(R.id.btnLogin).setOnClickListener() {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun signUpUser(){
        val email : String = findViewById<EditText>(R.id.etEmailAddress).text.toString()
        val password : String = findViewById<EditText>(R.id.etPassword).text.toString()
        val conformpassword : String = findViewById<EditText>(R.id.etConformPassword).text.toString()

        if (email.isBlank() || password.isBlank() || conformpassword.isBlank()) {
            Toast.makeText(this, "Email and Password can't be blank.", Toast.LENGTH_SHORT).show()
            return
        }
        if (password != conformpassword) {
            Toast.makeText(this, "Password and Conform Password did not match.", Toast.LENGTH_SHORT).show()
            return
        }
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){
            if(it.isSuccessful) {
                Toast.makeText(this, "SignUp Successful.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else {
                Toast.makeText(this, "Error creating user.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}