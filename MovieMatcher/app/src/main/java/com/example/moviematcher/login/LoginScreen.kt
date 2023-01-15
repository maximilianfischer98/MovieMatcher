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

import com.example.moviematcher.databinding.LoginViewBinding
import com.example.moviematcher.navigationbar.NavigationController

import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth



class LoginScreen : AppCompatActivity() {


    private lateinit var binding: LoginViewBinding

    private val firebaseAuth = FirebaseAuth.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding = LoginViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.newAccount.setOnClickListener{

            val intent = Intent(this, Registration::class.java)
            startActivity(intent)

        }




        binding.StartButton.setOnClickListener {

            var email = binding.email.text.toString()
           var  password = binding.password.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() ) {

                mainViewModel.loginUser(email,password)
                mainViewModel.loginResult.observe(this, Observer {
                    if (it) {
                        val intent = Intent(this, NavigationController::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "username or password wrong", Toast.LENGTH_LONG).show()
                    }
                })


            }
            else  if (email.isEmpty()) {
                Snackbar
                    .make(it,"Please enter username ", Snackbar.LENGTH_LONG)
                    .show()
            }

            else  if (password.isEmpty()) {
                Snackbar
                    .make(it,"Please enter password ", Snackbar.LENGTH_LONG)
                    .show()
            }

        }
    }




}