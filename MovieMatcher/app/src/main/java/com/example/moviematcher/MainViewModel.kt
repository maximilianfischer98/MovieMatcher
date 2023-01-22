package com.example.moviematcher

import MatchesModel
import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.moviematcher.data.User
import com.example.moviematcher.databinding.LoginViewBinding
import com.example.moviematcher.login.LoginScreen
import com.example.moviematcher.navigationbar.NavigationController
import com.example.moviematcher.navigationbar.friends.FriendsAdapter
import com.example.moviematcher.navigationbar.friends.FriendsModel
import com.example.moviematcher.navigationbar.matches.MatchesAdapter
import com.example.moviematcher.navigationbar.settings.settings
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class MainViewModel(application: Application): AndroidViewModel(application) {


    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var database: DatabaseReference

    private val _currentUserMail = MutableLiveData<String>()
    val currentUserMail: LiveData<String>
        get() = _currentUserMail

    private val _selectedMovie = MutableLiveData<String>()
    val selectedMovie: LiveData<String>
        get() = _selectedMovie

    private val _currentUserData = MutableLiveData<User?>()
    val currentUserData: LiveData<User?>
        get() = _currentUserData

    private val _friends = MutableLiveData<MutableList<FriendsModel>>()
    val friends: LiveData<MutableList<FriendsModel>>
        get() = _friends

    private val _matches = MutableLiveData<MutableList<MatchesModel>>()
    val matches: LiveData<MutableList<MatchesModel>>
        get() = _matches

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult : LiveData<Boolean>
        get() = _loginResult



    fun signUp(email: String, password: String, name: String,username: String) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                writeNewUserinDB(email,username,name)
                loginUser(email, password)


            } else {
                Timber.e("SignUp failed")
            }
        }
    }


        private fun writeNewUserinDB(email: String, username: String, name: String) {


            database = Firebase.database.reference
            val user = User(email,username,name)


            database.child("users").child(username).setValue(user)
                .addOnSuccessListener {
                    Timber.i("User created")

                }
                .addOnFailureListener {
                    Timber.e("failed to create User")
                }

        }


    fun loginUser(email:String,password:String){


        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(){
            if(it.isSuccessful){
                _loginResult.value = true
                _currentUserMail.value = firebaseAuth.currentUser!!.email

                Timber.i("Login sucessfull")

            }
            else {
                _loginResult.value = false
               // Log.e(ContentValues.TAG, "Login failed: ${it.exception}")
                Timber.e("Login failed")
            }
        }

    }

    fun loadFriends(currentMail: String) {
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

                    println("Mail"+currentUserMail.value)

                    if (email == currentMail) {
                        Timber.i("Found User in DB")
                        if (friend.contains(',')) {
                            friendList = friend.split(",") as ArrayList<String>
                        } else {
                            friendList.add(friend)
                        }
                    }
                }

                println("List1"+friendList)

                for (i in  friendList.indices) {
                    friendList[i] = friendList[i].removePrefix("[").removeSuffix("]")
                }

                for (i in friendList.indices) {
                    friendList[i] = friendList[i].trim()
                }

                println("List2"+friendList)
                _friends.value = friendList.map { FriendsModel(it) }.toMutableList()
                println("Friends"+ _friends)
            }

        })
    }

    fun getCurrentUserMail(): String? {
        return firebaseAuth.currentUser?.email
    }

    fun getCurrentUsername(callback: (String) -> Unit) {

        val currentuseremail = getCurrentUserMail()
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
                        println("Username: "+username)
                        callback(username)
                    }
                }
            }
        })
    }

    fun loadMatches(){


        val mDatabase = FirebaseDatabase.getInstance().reference
        _matches.value = mutableListOf()


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
                        if (username == username1 || username == username2) {
                            var foundMatch = false
                            if(_matches.value != null) {
                                for (currentMatch in _matches.value!!) {
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
                            }
                            if (!foundMatch) {

                                // Moviename does not exist in the list, so add a new MatchesModel object to the list
                                if (username1 == username ) {

                                    _matches.postValue(_matches.value?.apply { add(MatchesModel(moviename, arrayListOf(username2))) } )
                                } else if (username2 == username) {
                                    _matches.postValue(_matches.value?.apply { add(MatchesModel(moviename, arrayListOf(username1))) } )
                                }
                            }


                        }


                    }
                }

            }

        })


    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        _currentUserData.value = null

    }

    fun changePassword(currentPassword: String, newPassword: String, context:Context) {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val credential = EmailAuthProvider.getCredential(user?.email!!, currentPassword)

        user.reauthenticate(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("User re-authenticated")
                    user.updatePassword(newPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Timber.d("User password updated")
                                Toast.makeText(context, "Password Changed", Toast.LENGTH_SHORT).show()
                            } else {
                                Timber.e("Failed to update Password")

                            }
                        }
                } else {
                    Timber.e("Failed to re-authenticated user")
                    Toast.makeText(context, "The current password is incorrect.", Toast.LENGTH_SHORT).show()
                }
            }
    }


}