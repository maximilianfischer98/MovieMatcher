package com.example.moviematcher.navigationbar.friends

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.moviematcher.R
import com.example.moviematcher.databinding.ActivityAddfriendBinding
import com.example.moviematcher.navigationbar.NavigationController

class addFriend: AppCompatActivity() {


    private lateinit var binding: ActivityAddfriendBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddfriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = "Add a Friend"
        setSupportActionBar(binding.toolbar)

        binding.AddButton.setOnClickListener(){
            print("BUtton")
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.close_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.back -> {
                val i = Intent(this, NavigationController::class.java)
               // i.putExtra("frgToLoad", Fragment)

                startActivity(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}