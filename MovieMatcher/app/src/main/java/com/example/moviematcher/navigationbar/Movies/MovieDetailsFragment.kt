package com.example.moviematcher.navigationbar.Movies

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.moviematcher.R
import com.example.moviematcher.databinding.ActivityMovieDetailsBinding
import com.example.moviematcher.databinding.AddfriendBinding
import com.example.moviematcher.navigationbar.NavigationController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class MovieDetailsFragment : Fragment() {

    private lateinit var binding: ActivityMovieDetailsBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.anbieter.setOnClickListener {
            getLink { link ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(intent)
            }


        }

        setData()
    }



    fun setData() {

        val titel = arguments?.getString("key").toString()

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
        val titel = arguments?.getString("key").toString()

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