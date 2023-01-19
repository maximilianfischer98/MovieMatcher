package com.example.moviematcher.navigationbar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.moviematcher.MainApp
import com.example.moviematcher.R
import com.example.moviematcher.navigationbar.friends.Friends
import com.example.moviematcher.navigationbar.friends.FriendsModel
import com.example.moviematcher.navigationbar.matches.Matches
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber


class NavigationController : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setupNavigationController()
        Timber.i("setup of NavigationController successful")



    }

    fun setupNavigationController() {

        if (!isFinishing && !isDestroyed) {
            setContentView(R.layout.activity_navigation_controller)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragment)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.friends,R.id.movies,R.id.matches))
        setupActionBarWithNavController(navController,appBarConfiguration)

        bottomNavigationView.setupWithNavController(navController)

        //  app = requireActivity().application as MainApp




    }

//navigate to Fragement funktioniert nicht

    fun navigateToFragment( fragment: String) {
        val fragmentManager = supportFragmentManager
        val friendsFragment = Friends()
        val matchesFragment = Matches()
        val fragmentTransaction = fragmentManager.beginTransaction()
       // fragmentTransaction.add(R.id.constraint, Movies())
        when (fragment) {
            "Friends" -> fragmentTransaction.replace(R.id.containerMatches, friendsFragment)
            "Matches" -> fragmentTransaction.replace(R.id.containerMatches, matchesFragment)

        }
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}
