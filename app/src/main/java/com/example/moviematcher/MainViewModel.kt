package com.example.moviematcher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.moviematcher.login.LoginScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainViewModel:AppCompatActivity() {


    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
        }
    }


}