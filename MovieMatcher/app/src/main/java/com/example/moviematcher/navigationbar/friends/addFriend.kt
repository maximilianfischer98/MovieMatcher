package com.example.moviematcher.navigationbar.friends

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.moviematcher.MainApp
import com.example.moviematcher.R
import com.example.moviematcher.databinding.AddfriendBinding
import com.example.moviematcher.navigationbar.NavigationController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class addFriend: AppCompatActivity() {


    private lateinit var binding: AddfriendBinding
    val friends = ArrayList<FriendsModel>()
    lateinit var app: MainApp
    val mDatabase = FirebaseDatabase.getInstance().reference
    val currentuseremail = FirebaseAuth.getInstance().currentUser!!.email




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = application as MainApp

      //  binding = ActivityAddfriendBinding.inflate(layoutInflater)
        setContentView(binding.root)
    // Binding ToolbarName
        binding.toolbar.title = "Add a Friend"
        setSupportActionBar(binding.toolbar)




        binding.AddButton.setOnClickListener(){
            var friendname = binding.username.text.toString()


            if (friendname.isNotEmpty() ) {

                addFriend(friendname)

            }



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

    fun addFriend(friendUserName: String) {

        var currentusername = ""
        val usernameList = ArrayList<String>()
        val friendList = ArrayList<String>()
        val constraintLayout = binding.constraint
        var y = 1



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
                        var name = it.child("name").value.toString()
                        var friend = it.child("friends").child(friendUserName).value.toString()

                        if(email == currentuseremail){
                            //Get the current username
                            currentusername = username
                        //Just add User of current User to list
                                friendList.add(friend)
                        }

                        usernameList.add(username)



                    }



                    println("Current User" + currentusername)



                    if(usernameList.contains(friendUserName) && currentusername != friendUserName && !friendList.contains(friendUserName) ) {
                        //Add Friend at CurrentUser Friendslist
                        mDatabase.child("users").child(currentusername).child("friends")
                            .child(friendUserName).setValue(friendUserName)
                        //Add current User at Friends Friendslist
                        mDatabase.child("users").child(friendUserName).child("friends")
                            .child(currentusername).setValue(currentusername)


                        //Übergangslösung muss durch die Aktualisierung des RecylerViews ersetzt werden
                        app = application as MainApp
                        app.friends.add(FriendsModel(friendUserName))

                        val i = Intent(getApplicationContext(), NavigationController::class.java)

                        startActivity(i)

                    }
                    else if(currentusername == friendUserName){
                        println("User can not add himself")
                        Snackbar
                            .make(constraintLayout,R.string.user_can_not_add_himself, Snackbar.LENGTH_LONG)
                            .show()

                    }
                    else if(friendList.contains(friendUserName)){
                        Snackbar
                            .make(constraintLayout,R.string.user_already_exist, Snackbar.LENGTH_LONG)
                            .show()
                    }
                    else{
                        println("User does not exist")
                        Snackbar
                            .make(constraintLayout,R.string.user_not_in_list, Snackbar.LENGTH_LONG)
                            .show()
                    }



                }
            })
            y++

    }





}