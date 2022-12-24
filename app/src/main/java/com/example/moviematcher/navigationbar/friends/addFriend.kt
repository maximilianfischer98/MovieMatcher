package com.example.moviematcher.navigationbar.friends

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.moviematcher.R
import com.example.moviematcher.data.Movie
import com.example.moviematcher.data.User
import com.example.moviematcher.databinding.ActivityAddfriendBinding
import com.example.moviematcher.navigationbar.NavigationController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class addFriend: AppCompatActivity() {


    private lateinit var binding: ActivityAddfriendBinding
    private lateinit var database: DatabaseReference
    private val firebaseAuth = FirebaseAuth.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityAddfriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = "Add a Friend"
        setSupportActionBar(binding.toolbar)



        binding.AddButton.setOnClickListener(){
            var friendname = binding.username.text.toString()
            addFriend(friendname)

            val i = Intent(this, NavigationController::class.java)
            // i.putExtra("frgToLoad", Fragment)

            startActivity(i)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.close_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


                val i = Intent(this, NavigationController::class.java)
               // i.putExtra("frgToLoad", Fragment)

                startActivity(i)


       return true
    }

    fun addFriend(name: String){
        val mDatabase = FirebaseDatabase.getInstance().reference
        val currentuseremail = FirebaseAuth.getInstance().currentUser!!.email
        var currentusername = ""

       // mDatabase.child("users").child("").child("Friends").setValue(name);




            val listofallUserNames = ArrayList<String>()
             val listofallUserEmails = ArrayList<String>()

            val rootRef = mDatabase.child("users")
            rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    println("Nicht geklappt")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val children = snapshot!!.children
                    children.forEach {
                        var username = it.key.toString()
                        var email = it.child("email").value.toString()

                        if(email == currentuseremail){
                            currentusername = username

                        }

                       // listofallUserNames.add(username)
                        //listofallUserEmails.add(email)

                    }
                    println("username: "+ currentusername)

                    mDatabase.child("users").child(currentusername).child("friends").child(name).setValue(name)


                }
            })






    }
}