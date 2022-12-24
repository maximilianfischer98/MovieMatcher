package com.example.moviematcher.login

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviematcher.FirebaseDB
import com.example.moviematcher.R
import com.example.moviematcher.choosecategory.ChooseCategoryList
import com.example.moviematcher.data.User
import com.example.moviematcher.databinding.ActivityRegistrationBinding
import com.example.moviematcher.navigationbar.NavigationController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase



class Registration: AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    private val firebaseAuth = FirebaseAuth.getInstance()

   private lateinit var database: DatabaseReference





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = "Create new User"
        setSupportActionBar(binding.toolbar)


        binding.StartButton.setOnClickListener {

                var email = binding.email.text.toString()
                var password = binding.password.text.toString()
                var username = binding.username.text.toString()
                var lastname = binding.name.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty() ) {

                createUser(email, password)
                    writeNewUserinDB(email,username,lastname)


                }

                else  if (email.isEmpty()) {

                    Snackbar
                        .make(it,"Please enter password ", Snackbar.LENGTH_LONG)
                        .show()

                }

                else  if (password.isEmpty()) {
                    Snackbar
                        .make(it,"Please enter password ", Snackbar.LENGTH_LONG)
                        .show()
                }




        }


    }

    fun createUser(email: String,password: String){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {

                val intent = Intent(this, LoginScreen::class.java)
                Toast.makeText(this@Registration, "Account created", Toast.LENGTH_LONG).show()
                startActivity(intent)


            } else {

                Log.e(ContentValues.TAG, "SignUp failed: ${it.exception}")
                Toast.makeText(this@Registration, "Account can not be created", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun writeNewUserinDB(email: String, username: String, name: String) {


        database = Firebase.database.reference

        val user = User(email, username,name)


        database.child("users").child(username).setValue(user)
            .addOnSuccessListener {
                //Log ergänzen
                print("User createded")
            }
            .addOnFailureListener {
                print("User not createded")
            }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.close_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.back -> {
                val i = Intent(this, LoginScreen::class.java)


                startActivity(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }


}


