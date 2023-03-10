package com.example.moviematcher.login

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.moviematcher.MainViewModel
import com.example.moviematcher.R
import com.example.moviematcher.databinding.ActivityRegistrationBinding
import com.example.moviematcher.navigationbar.NavigationController
import com.github.ajalt.timberkt.Timber

import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase



class Registration: AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = "Create new User"
        setSupportActionBar(binding.toolbar)


        binding.StartButton.setOnClickListener {

                var email = binding.email.text.toString()
                var password = binding.password.text.toString()
                var username = binding.username.text.toString()
                var name = binding.name.text.toString()





                if (email.isNotEmpty() && password.isNotEmpty() ) {


                    mainViewModel.signUp(email,password,name,username)
                    mainViewModel.loginResult.observe(this, Observer {
                        if (it) {
                            val intent = Intent(this, NavigationController::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, R.string.username_or_password_empty, Toast.LENGTH_LONG).show()
                            Timber.w(null, { "username or password empty" })
                        }
                    })

                }

                else  if (email.isEmpty()) {

                    Snackbar
                        .make(it,R.string.please_enter_email, Snackbar.LENGTH_LONG)
                        .show()
                    Timber.w(null, { "User doens't enterd email" })

                }

                else  if (password.isEmpty()) {
                    Snackbar
                        .make(it,R.string.please_enter_password , Snackbar.LENGTH_LONG)
                        .show()
                    Timber.w(null, { "User doens't entered password" })
                }

                else  if (username.isEmpty()) {
                    Snackbar
                        .make(it,R.string.please_enter_username, Snackbar.LENGTH_LONG)
                        .show()
                    Timber.w(null, { "User doens't entered username" })
                }

                else  if (name.isEmpty()) {
                    Snackbar
                        .make(it,R.string.please_enter_name , Snackbar.LENGTH_LONG)
                        .show()
                    Timber.w(null, { "User doens't entered name" })
                }


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


