package com.example.mobileappdevtest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Link the XML layout
        // Initialize buttons
        val loginButton: Button = findViewById(R.id.btnLogin)
        val signupButton: Button = findViewById(R.id.btnSignup)
        val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // User is logged in, redirect to HomePage
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
            finish() // Close MainActivity to prevent going back
        }
        // Set onClick listeners for buttons
        loginButton.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        signupButton.setOnClickListener {
            val intent = Intent(this, Signip::class.java)
            startActivity(intent)
        }
    }
}
