package com.example.moviematcher.navigationbar.friends

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.moviematcher.MainApp
import com.example.moviematcher.R
import com.example.moviematcher.databinding.AddfriendBinding
import com.example.moviematcher.databinding.VideoplayerBinding
import com.example.moviematcher.navigationbar.NavigationController
import com.github.ajalt.timberkt.Timber
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class addFriendFragment: Fragment() {


    private lateinit var binding: AddfriendBinding
    val friends = ArrayList<FriendsModel>()
    val mDatabase = FirebaseDatabase.getInstance().reference
    val currentuseremail = FirebaseAuth.getInstance().currentUser!!.email



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = AddfriendBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.AddButton.setOnClickListener(){
            val friendname = binding.username.text.toString()


            if (friendname.isNotEmpty() ) {

                addFriend(friendname)
                Timber.i(null, { "Try to add Friend" })

            }


        }
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
                    Timber.e(null, { "Database Error" })
                }




                override fun onDataChange(snapshot: DataSnapshot) {
                    val children = snapshot!!.children
                    children.forEach {
                        var username = it.key.toString()
                        var email = it.child("email").value.toString()
                        var friend = it.child("friends").child(friendUserName).value.toString()

                        if(email == currentuseremail){
                            Timber.i(null, { "Found current User in DB" })
                            //Get the current username
                            currentusername = username
                        //Just add User of current User to list
                                friendList.add(friend)
                        }

                        usernameList.add(username)



                    }



                    // checks if User is already in List
                    if(usernameList.contains(friendUserName) && currentusername != friendUserName && !friendList.contains(friendUserName) ) {
                        //Add Friend at CurrentUser Friendslist
                        mDatabase.child("users").child(currentusername).child("friends")
                            .child(friendUserName).setValue(friendUserName)
                        //Add current User at Friends Friendslist
                        mDatabase.child("users").child(friendUserName).child("friends")
                            .child(currentusername).setValue(currentusername)

                        Timber.i(null, { "Added new Friend" })

                        findNavController().navigate(R.id.friends)

                    }
                    else if(currentusername == friendUserName){
                        Snackbar
                            .make(constraintLayout,R.string.user_can_not_add_himself, Snackbar.LENGTH_LONG)
                            .show()
                        Timber.w(null, { "User tryed to add himself" })

                    }
                    else if(friendList.contains(friendUserName)){
                        Snackbar
                            .make(constraintLayout,R.string.user_already_exist, Snackbar.LENGTH_LONG)
                            .show()
                        Timber.w(null, { "User tryed to add a person who is already friend" })
                    }
                    else{
                        Snackbar
                            .make(constraintLayout,R.string.user_not_in_list, Snackbar.LENGTH_LONG)
                            .show()
                        Timber.w(null, { "User tryed to add a person who not exist" })
                    }



                }
            })
            y++

    }





}