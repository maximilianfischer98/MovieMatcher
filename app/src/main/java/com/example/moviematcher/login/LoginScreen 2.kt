package com.example.moviematcher.login

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moviematcher.R
import com.example.moviematcher.choosecategory.ChooseCategoryList
import com.example.moviematcher.databinding.ActivityRegistrationBinding
import com.example.moviematcher.databinding.LoginViewBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.core.View


class LoginScreen : AppCompatActivity() {


    private lateinit var binding: LoginViewBinding


    private val firebaseAuth = FirebaseAuth.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

               loginUser(email, password)

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

    fun loginUser(email:String,password:String){

        binding = LoginViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(){
            if(it.isSuccessful){
                val intent = Intent(this, ChooseCategoryList::class.java)

                startActivity(intent)

            }
            else {

                Log.e(ContentValues.TAG, "Login failed: ${it.exception}")
                Toast.makeText(this@LoginScreen, "username or password wrong", Toast.LENGTH_LONG).show()

            }
        }

    }


}