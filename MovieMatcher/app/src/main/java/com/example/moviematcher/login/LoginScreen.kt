package com.example.moviematcher.login

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.moviematcher.MainViewModel
import com.example.moviematcher.R

import com.example.moviematcher.databinding.LoginViewBinding
import com.example.moviematcher.navigationbar.NavigationController
import com.github.ajalt.timberkt.Timber

import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth



class LoginScreen : AppCompatActivity() {


    private lateinit var binding: LoginViewBinding




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding = LoginViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //checks if User is not null
        ifUserIsLoggedIn()


        binding.newAccount.setOnClickListener{

            val intent = Intent(this, Registration::class.java)
            startActivity(intent)

        }




        binding.StartButton.setOnClickListener {

            val email = binding.email.text.toString()
           val  password = binding.password.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() ) {

                mainViewModel.loginUser(email,password)
                mainViewModel.loginResult.observe(this, Observer {
                    if (it) {
                        val intent = Intent(this, NavigationController::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, R.string.username_OR_password_wrong, Toast.LENGTH_LONG).show()
                        Timber.e(null, { "Not possible to login user" })
                    }
                })


            }
            else  if (email.isEmpty()) {
                Snackbar
                    .make(it,R.string.please_enter_username , Snackbar.LENGTH_LONG)
                    .show()
                Timber.w(null, { "User doens't enterd email" })
            }

            else  if (password.isEmpty()) {
                Snackbar
                    .make(it,R.string.please_enter_password, Snackbar.LENGTH_LONG)
                    .show()
                Timber.w(null, { "User doens't enterd password" })
            }

        }



    }

    fun ifUserIsLoggedIn(){
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            // User is already signed in, redirect to home screen
            startActivity(Intent(this, NavigationController::class.java))
            finish()
        }
    }



}