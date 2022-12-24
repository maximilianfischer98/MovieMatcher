package com.example.moviematcher

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviematcher.databinding.VideoplayerBinding
import com.example.moviematcher.navigationbar.Movies
import com.example.moviematcher.navigationbar.NavigationController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class VideoPlayer: AppCompatActivity() {

    private lateinit var binding: VideoplayerBinding
    var movieid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = VideoplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = "MovieTrailer"
        setSupportActionBar(binding.toolbar)





        val extras = intent.extras
        if (extras != null) {
            movieid = extras.getString("key").toString()

        }

        manageYoutubePlayer(movieid)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.close_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val i = Intent(this, NavigationController::class.java)


        startActivity(i)


        return true
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




