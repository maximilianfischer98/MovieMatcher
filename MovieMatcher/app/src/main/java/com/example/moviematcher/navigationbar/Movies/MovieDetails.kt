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
import com.github.ajalt.timberkt.Timber
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


        binding.anbieter.setOnClickListener {
            getLink { link ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(intent)
                Timber.i(null, { "Start Netflix Link" })
            }


        }

        setData()

    }



    fun setData() {

        val titel = intent.getStringExtra("text")

        println("text"+ titel);


        val mDatabase = FirebaseDatabase.getInstance().reference

        val rootRef = mDatabase.child("movies")
        rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Timber.e(null, { "Database Error" })
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot!!.children
                children.forEach {
                    val moviename = it.key.toString()
                    val description = it.child("description").value.toString()
                    val rating = it.child("ImdB").value.toString()
                    val movieId = it.child("movieId").value.toString()

                    println("Moviename"+ moviename)
                    if(titel == moviename){
                        println("Titel =Moviename"+ moviename)
                        binding.titel.text = moviename
                        binding.description.text = description
                        binding.rating.text = rating
                        manageYoutubePlayer(movieId)
                        Timber.i(null, { "Found Movie in DB" })
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
                Timber.e(null, { "Database Error" })
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot!!.children
                children.forEach {
                    val moviename = it.key.toString()
                    val link = it.child("link").value.toString()
                    if (titel == moviename) {
                        callback(link)
                        Timber.i(null, { "Callback Movie Link" })
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
                Timber.i(null, { "Trailer ready to start" })
            }
        })
        youTubePlayerView.enterFullScreen()

    }




}