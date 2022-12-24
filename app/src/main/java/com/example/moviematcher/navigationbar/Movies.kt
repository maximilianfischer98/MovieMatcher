
package com.example.moviematcher.navigationbar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.moviematcher.VideoPlayer
import com.example.moviematcher.data.Movie
import com.example.moviematcher.databinding.FragmentMoviesBinding
import com.example.moviematcher.login.Registration
import com.example.moviematcher.navigationbar.friends.addFriend
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class Movies: Fragment() {

    private lateinit var binding: FragmentMoviesBinding

    private lateinit var database: DatabaseReference

    lateinit var videoId: String;

    val movieList= ArrayList<Movie>()

    var i = 0;

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMoviesBinding.inflate(inflater)
        readMovieData()


        binding.buttonNo.setOnClickListener{
            i++;

            readMovieData()
         //   var moviepicture = binding.imageView
          //  Glide.with(this).load(moviePictureURL).into(moviepicture)

        }

        binding.PlayButton.setOnClickListener{
            val value = videoId
            val i = Intent(activity, VideoPlayer::class.java)
            i.putExtra("key", value)
            startActivity(i)



        }








        return binding.root

    }

  /*  fun manageYoutubePlayer(movieId:String){
        val youTubePlayerView: YouTubePlayerView = binding.youtubePlayerView
        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.invalidate()

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                videoId = movieId
                youTubePlayer.loadVideo(videoId, 0f)
                youTubePlayer.cueVideo(videoId,0f)
            }
        })
    }*/

    fun readMovieData(){
        database = Firebase.database.reference

        val mDatabase = FirebaseDatabase.getInstance().reference
        var moviename = ""
        var moviedescription = ""
        var movieid = ""
        var moviepicture = ""

        val rootRef = mDatabase.child("movies")
        rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                println("Nicht geklappt")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot!!.children
                children.forEach {
                    println(it.toString())
                    moviename = it.key.toString()
                    moviedescription = it.child("description").value.toString()
                    movieid = it.child("movieId").value.toString()
                    moviepicture = it.child("moviepicture").value.toString()
                    var movie = Movie(moviename,moviedescription,movieid,moviepicture)

                    movieList.add(movie)




                }
                binding.titel.setText(movieList[i].moviename)
                binding.description.setText(movieList[i].description)
                videoId = movieList[i].movieid
                var moviepicture = binding.imageView
                Glide.with(this@Movies).load(movieList[i].moviePictureURL).into(moviepicture)




            }

        })




    }






}









