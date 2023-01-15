package com.example.moviematcher


import MatchesModel
import android.app.Application
import com.example.moviematcher.navigationbar.friends.FriendsModel
import com.example.moviematcher.navigationbar.matches.MatchesAdapter
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import timber.log.Timber


class MainApp : Application() {




   val friends = ArrayList<FriendsModel>()
    var matches = ArrayList<MatchesModel>()



    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        FirebaseApp.initializeApp(this)

        //Gets Friends of current User from DB
       // addFriends()
        //Gets Matches of current User from DB
        addMatches()




    }

    fun getCurrentUsername(callback: (String) -> Unit) {

        val currentuseremail = FirebaseAuth.getInstance().currentUser!!.email
        val mDatabase = FirebaseDatabase.getInstance().reference

        val rootRef = mDatabase.child("users")
        rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Timber.e("Failed to get current Username from Database")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot!!.children
                children.forEach {
                    val username = it.key.toString()
                    val email = it.child("email").value.toString()

                    if (email == currentuseremail) {
                        callback(username)
                    }
                }
            }
        })
    }

    fun addMatches(){

        val currentuseremail = FirebaseAuth.getInstance().currentUser!!.email
        val mDatabase = FirebaseDatabase.getInstance().reference



        val rootRef = mDatabase.child("matches")
        rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Timber.e("Failed to get Matches of current User from DB")
            }


            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot!!.children
                children.forEach {
                    var moviename = it.child("moviename").value.toString()
                    var username1 = it.child("username1").value.toString()
                    var username2 = it.child("username2").value.toString()

                    getCurrentUsername { username ->
                        // The callback function will be called with the current username as the argument
                        println("current  " + username)
                        if (username == username1 || username == username2) {
                            println("username1 " + username1 + "username2 " + username2)
                            var foundMatch = false
                            for (currentMatch in matches) {
                                if (currentMatch.moviename == moviename) {
                                    // Moviename already exists in the list, so just add the username to the list of usernames
                                    if (currentMatch.moviename == moviename) {
                                        if (username1 == username) {
                                            if (!currentMatch.friends.contains(username2)) {
                                                currentMatch.friends.add(username2)
                                            }
                                        } else if (username2 == username) {
                                            if (!currentMatch.friends.contains(username1)) {
                                                currentMatch.friends.add(username1)
                                            }
                                        }
                                        foundMatch = true
                                        break
                                    }


                                }
                            }
                            if (!foundMatch) {
                                // Moviename does not exist in the list, so add a new MatchesModel object to the list
                                if (username1 == username ) {
                                    matches.add(MatchesModel(moviename, arrayListOf(username2)))
                                } else if (username2 == username) {
                                    matches.add(MatchesModel(moviename, arrayListOf(username1)))
                                }
                            }
                            val matchesAdapter = MatchesAdapter(matches)
                            //matchesAdapter.updateData(matches)

                        }


                        // matches = matchesList.distinct() as ArrayList<MatchesModel>


                    }
                }

                }

        })


    }


    fun addFriends() {

        val currentuseremail = FirebaseAuth.getInstance().currentUser!!.email
        val mDatabase = FirebaseDatabase.getInstance().reference

        var friendList = ArrayList<String>()





        val rootRef = mDatabase.child("users")
        rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Timber.e("Failed to get Friends of current User from DB")
            }


        override fun onDataChange(snapshot: DataSnapshot) {
            val children = snapshot!!.children
            children.forEach {
                var username = it.key.toString()
                var email = it.child("email").value.toString()
                var name = it.child("name").value.toString()
                var friend = it.child("friends").children.map { it.key }.toString()


                if (email == currentuseremail) {

                    if (friend.contains(',')) {
                        friendList = friend.split(",") as ArrayList<String>
                    }
                    else{

                        friendList.add(friend)
                    }

                }

            }

            println("List1" + friendList)

            for (i in  friendList.indices) {
                friendList[i] = friendList[i].removePrefix("[").removeSuffix("]")
            }

            for (i in friendList.indices) {
                friendList[i] = friendList[i].trim()
            }


                for (friend in friendList) {
                    friends.add(FriendsModel(friend))

                }





        }



    })

    }




}