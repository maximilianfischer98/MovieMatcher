package com.example.moviematcher

import com.example.moviematcher.data.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

abstract class FirebaseDB {


    private lateinit var database: DatabaseReference


    fun initializeDbRef() {
        database = Firebase.database.reference
    }


    fun writeNewUserWithTaskListeners(email: String, firstname: String, lastname: String,friends: ArrayList<String>) {
        val user = User(email, firstname,lastname,friends)


        database.child("users").child(email).setValue(user)
            .addOnSuccessListener {
                //Log ergänzen
            }
            .addOnFailureListener {

            }

    }
}