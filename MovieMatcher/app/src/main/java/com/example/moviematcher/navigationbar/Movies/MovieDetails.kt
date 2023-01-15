package com.example.moviematcher.navigationbar.Movies

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.moviematcher.R
import com.example.moviematcher.databinding.ActivityMovieDetailsBinding
import com.example.moviematcher.navigationbar.NavigationController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class MovieDetails : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val titel = intent.getStringExtra("text")

        //Geht aus irgendeinem Grund nicht
        binding.toolbar.title = "Movie Details"
        binding.toolbar.setBackgroundColor(getResources().getColor(R.color.darkgreytransparent));


        binding.anbieter.setOnClickListener {
            getLink { link ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(intent)
            }


        }

        setData()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.close_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        val i = Intent(this, NavigationController::class.java)
        i.putExtra("frgToLoad", "Matches")



        startActivity(i)


        return true
    }

    fun setData() {

        val titel = intent.getStringExtra("text")

        println("text"+ titel);


        val mDatabase = FirebaseDatabase.getInstance().reference

        val rootRef = mDatabase.child("movies")
        rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                println("Nicht geklappt")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot!!.children
                children.forEach {
                    val moviename = it.key.toString()
                    val description = it.child("description").value.toString()
                    val rating = it.child("ImdB").value.toString()
                    val movieId = it.child("movieId").value.toString()
                    val pictureUrl = it.child("moviepicture").value.toString()
                    val link = it.child("link").value.toString()

                    println("Moviename"+ moviename)
                    if(titel == moviename){
                        println("Titel =Moviename"+ moviename)
                        binding.titel.text = moviename
                        binding.description.text = description
                        binding.rating.text = rating
                        manageYoutubePlayer(movieId)

                    }



                }
            }
        })


    }

    fun getLink(callback: (link: String) -> Unit) {
        val titel = intent.getStringExtra("text")

        val mDatabase = FirebaseDatabase.getInstance().reference
        val rootRef = mDatabase.child("movies")
        rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                println("Nicht geklappt")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot!!.children
                children.forEach {
                    val moviename = it.key.toString()
                    val link = it.child("link").value.toString()
                    if (titel == moviename) {
                        callback(link)
                    }
                }
            }
        })
    }


    fun manageYoutubePlayer(movieId: String) {
        val youTubePlayerView: YouTubePlayerView = binding.youtubePlayerView
        lifecycle.addObserver(youTubePlayerView)



        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(movieId, 0f)
                youTubePlayer.cueVideo(movieId, 0f)
            }
        })
        youTubePlayerView.enterFullScreen()

    }




}