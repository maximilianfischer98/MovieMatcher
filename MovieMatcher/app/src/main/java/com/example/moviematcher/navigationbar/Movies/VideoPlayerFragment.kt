package com.example.moviematcher.navigationbar.Movies

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.moviematcher.R
import com.example.moviematcher.databinding.FragmentMatchesBinding
import com.example.moviematcher.databinding.VideoplayerBinding
import com.example.moviematcher.navigationbar.NavigationController
import com.github.ajalt.timberkt.Timber
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class VideoPlayerFragment: Fragment() {

    private lateinit var binding: VideoplayerBinding
    var movieid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = VideoplayerBinding.inflate(inflater, container, false)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "MovieTrailer"
        movieid = arguments?.getString("key").toString()
        manageYoutubePlayer(movieid)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.close_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        findNavController().navigate(R.id.movies)
        return true
    }



    fun manageYoutubePlayer(movieId: String) {
        val youTubePlayerView: YouTubePlayerView = binding.youtubePlayerView
        lifecycle.addObserver(youTubePlayerView)



        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(movieId, 0f)
                youTubePlayer.cueVideo(movieId, 0f)
                Timber.i(null, { "Movie ready to start" })
            }
        })
        youTubePlayerView.enterFullScreen()

    }
}




