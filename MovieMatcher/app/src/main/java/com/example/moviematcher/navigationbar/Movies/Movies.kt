
package com.example.moviematcher.navigationbar.Movies

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.moviematcher.MainApp
import com.example.moviematcher.R
import com.example.moviematcher.data.Match
import com.example.moviematcher.data.Movie
import com.example.moviematcher.data.UserLikeMovie
import com.example.moviematcher.databinding.FragmentMoviesBinding
import com.example.moviematcher.databinding.ItIsAMatchPopupwindowBinding
import com.example.moviematcher.navigationbar.NavigationController
import com.example.moviematcher.navigationbar.matches.Matches

import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit


class Movies: Fragment() {

    private lateinit var binding: FragmentMoviesBinding


    private lateinit var database: DatabaseReference

    val currentuseremail = FirebaseAuth.getInstance().currentUser!!.email
    val mDatabase = FirebaseDatabase.getInstance().reference
    val mainApp = MainApp()


    lateinit var videoId: String;

    var movieList= ArrayList<Movie>()


    var i = 0; // counter for movie who is showed



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
            userDidDecision(false)



        }

        binding.buttonYes.setOnClickListener{
            i++;
            userDidDecision(true)
            mainApp.addMatches()

    }

        binding.PlayButton.setOnClickListener{
            val value = videoId
            //val i = Intent(activity, VideoPlayer::class.java)
            //i.putExtra("key", value)
            //startActivity(i)
            val bundle = Bundle()
            bundle.putString("key", value)
            findNavController().navigate(R.id.videoPlayerFragment,bundle)


        }


        return binding.root

    }



    fun readMovieData(){
        database = Firebase.database.reference

        val mDatabase = FirebaseDatabase.getInstance().reference
        var moviename = ""
        var moviedescription = ""
        var movieid = ""
        var moviepicture = ""
        var rating = ""

        val constraintLayout = binding.constraint



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
                    rating = it.child("ImdB").value.toString()
                    var movie = Movie(moviename,moviedescription,movieid,moviepicture,rating)




                    if(!movieList.contains(movie)) {
                        movieList.add(movie)
                    }



                }


                movieAlreadyWatched { movieAlreadWatchedList ->


                    while (i <movieList.count() && movieAlreadWatchedList.contains(movieList[i].moviename ) ) {
                        i++;
                        println("i="+i)
                    }

                    println("MovieCount" + movieList.count ())

                    if (i >= movieList.size) {
                        Snackbar
                            .make(constraintLayout, R.string.all_movies_watched, Snackbar.LENGTH_LONG)
                            .show()
                        binding.titel.setText("All Movies Played")
                        binding.description.setVisibility(View.GONE)
                        binding.buttonNo.setVisibility(View.GONE)
                        binding.buttonYes.setVisibility(View.GONE)
                        binding.PlayButton.setVisibility(View.GONE)
                        binding.star.setVisibility(View.GONE)
                        binding.rating.setVisibility(View.GONE)

                        var moviepicture = binding.imageView
                        Glide.with(this@Movies).load("https://thumbs.dreamstime.com/b/gelber-netter-trauriger-emoticon-mit-riss-smiley-gesicht-gefühlen-gesichtsausdruck-stimmung-realistisches-emoji-d-lustig-132957441.jpg")
                            .into(moviepicture)
                    }


                    if(i< movieList.size){


                        println("Neuer Movie" + movieList[i].moviename)

                        binding.titel.setText(movieList[i].moviename)
                        binding.description.setText(movieList[i].description)
                        videoId = movieList[i].movieid
                        binding.rating.setText(movieList[i].rating)

                        var moviepicture = binding.imageView
                        if (isAdded()) {
                            Glide.with(this@Movies).load(movieList[i].moviePictureURL)
                                .into(moviepicture)
                        }
                    }




                }








            }

        })




    }

    fun checkIfItIsAMatch(){

        var moviesUsersLikedList = arrayListOf<UserLikeMovie>()
        var moviesCurrenUserLikedList = arrayListOf<UserLikeMovie>()
        var currentUser = ""
        var friendsOfCurrentUser = ArrayList<String>()

        val rootRef = mDatabase.child("users")
        rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                println("Nicht geklappt")
            }


            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot!!.children
                children.forEach {
                    var username = it.key.toString()
                    var email = it.child("email").value.toString()
                    var movie = it.child("moviesUserLiked").value.toString()
                    var friend = it.child("friends").value.toString()

                    if (email == currentuseremail) {

                    currentUser = username


                        //remove the first and last characters from the movie and split string by ,
                        val movies = movie.substring(1, movie.length - 1).split(", ")
                        //Trims and removes duplicates from list of strings split at equal signs.
                        val trimmedMovies = movies.map { it.split("=")[0].trim() }.toSet().toList()



                        for(movie in trimmedMovies) {

                            var likedMovie = UserLikeMovie(movie, username)
                            moviesCurrenUserLikedList.add(likedMovie)

                        }


                            //Create list from string input and remove duplicates.
                            val friendInputList = friend.trim('{', '}').split(",")
                            println("FriendInput"+ friendInputList)
                        //checks if Friend exist
                        if (friendInputList.size >= 1) {
                            friendsOfCurrentUser = friendInputList.toSet().map { it.split("=")[1] } as ArrayList<String>
                        }








                    }

                    else {

                        //remove the first and last characters from the movie and split string by ,
                        val movies = movie.substring(1, movie.length - 1).split(", ")
                        //Trims and removes duplicates from list of strings split at equal signs.
                        val trimmedMovies = movies.map { it.split("=")[0].trim() }.toSet().toList()


                        for(movie in trimmedMovies) {
                            var likedMovie = UserLikeMovie(movie, username)
                            moviesUsersLikedList.add(likedMovie)
                        }

                    }

                }






                println("1"+moviesCurrenUserLikedList)
                println("2"+moviesUsersLikedList)


                // saves all Movies who have a Match with the Usernames in a List
                val matchingMovies = moviesCurrenUserLikedList.flatMap { currentUserMovie ->
                    val otherUserMovies = moviesUsersLikedList.filter { it.moviename == currentUserMovie.moviename }
                    otherUserMovies.map { Match(currentUserMovie.moviename, currentUserMovie.username, it.username) }
                }

                println("Friendlist"+ friendsOfCurrentUser)

                //checks if a friend has same movie in movieLikedList

                if(checkIfItisAFriend(matchingMovies,friendsOfCurrentUser) == true) {

                    println("1"+moviesCurrenUserLikedList)
                    println("2"+moviesUsersLikedList)




                    //filter Matches of Movies who Match-Partner is not a Friend
                    val filteredMatchingMovies = matchingMovies
                        .filter { it.username1 != it.username2 } // exclude matches where the same user has liked the movie
                        .filter { it.username2 in friendsOfCurrentUser } // include only friends of the current user
                        .filter { it.moviename == binding.titel.text } // include only movies that have been liked by the specified movie


                    //Write Matches in DB
                    for (match in filteredMatchingMovies) {
                        writeNewMatchinDB(match.moviename, match.username1, match.username2)
                    }






                    if (filteredMatchingMovies.any { it.moviename == binding.titel.text.toString() }) {

                        //Filer User which are in Friendlist and Liked the Movie and are not current User
                        val matchingUsernames = filteredMatchingMovies
                            .filter { it.username1 != currentUser || it.username2 != currentUser }
                            .flatMap { listOf(it.username1, it.username2) }
                            .distinct()
                            .filter { it in friendsOfCurrentUser }
                            .filter { it != currentUser }



                        println("matchingMovies"+ matchingMovies)
                        println("matchingUsers" + matchingUsernames)

                        val index =
                            matchingMovies.indexOfFirst { it.moviename == binding.titel.text.toString() }


                        print("true")
                        context?.let {
                            showPopup(
                                it,
                                binding.titel.text as String,
                                matchingUsernames
                            )
                        }


                    }
                }


                println("Bining Titel" + binding.titel.text  )


            }






        })


        readMovieData()



    }

    fun writeNewMatchinDB(moviename: String, username1: String, username2: String) {


        database = Firebase.database.reference
        val match = Match(moviename,username1,username2)


        database.child("matches").child(moviename+"-"+username1+"-"+username2).setValue(match)
            .addOnSuccessListener {
                //Log ergänzen
                print("User createded")
            }
            .addOnFailureListener {
                print("User not createded")
            }

    }

    @SuppressLint("ResourceAsColor")
    fun showPopup(context: Context, movietitel: String, usernames: List<String>) {
        val view = LayoutInflater.from(context).inflate(R.layout.it_is_a_match_popupwindow, null)
        val bindingPopup = ItIsAMatchPopupwindowBinding.bind(view)
        bindingPopup.popupImageView.setImageDrawable(binding.imageView.drawable)
        bindingPopup.popupTitle.text = "It's a Match!"
        bindingPopup.popupMessage.text = Html.fromHtml("<b>Movietitel:</b> $movietitel<br><b>Matching Users:</b> $usernames")

        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()

        val party = Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
            position = Position.Relative(0.5, 0.3)
        )
        bindingPopup.konfettiView.start(party)
    }

    fun checkIfItisAFriend(matchingMovies: List<Match>, friendlist: List<String>) : Boolean {
        return matchingMovies.any { it.username1 in friendlist || it.username2 in friendlist }
    }





    fun movieAlreadyWatched(callback: (List<String>) -> Unit){


        var movieAlreadyWatchedList = ArrayList<String>()

        val rootRef = mDatabase.child("users")
        rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                println("Nicht geklappt")
            }


            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot!!.children
                children.forEach {
                    var email = it.child("email").value.toString()
                    var movies = it.child("moviesalreadywatched").children.map { it.key }.toString()

                    if (email == currentuseremail) {
                        if (movies.contains(',')) {
                            movieAlreadyWatchedList = movies.split(",") as ArrayList<String>
                        }
                        else{
                            movieAlreadyWatchedList.add(movies)
                        }
                    }



                }
                for (i in  movieAlreadyWatchedList.indices) {
                    movieAlreadyWatchedList[i] = movieAlreadyWatchedList[i].removePrefix("[").removeSuffix("]")
                }

                for (i in movieAlreadyWatchedList.indices) {
                    movieAlreadyWatchedList[i] = movieAlreadyWatchedList[i].trim()
                }


                if (movieAlreadyWatchedList != null) {
                    callback(movieAlreadyWatchedList)
                }


            }



        })

    }





    fun userDidDecision(userlikedmovie: Boolean) {



        val rootRef = mDatabase.child("users")
        rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                println("Nicht geklappt")
            }


            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot!!.children
                children.forEach {
                    var username = it.key.toString()
                    var email = it.child("email").value.toString()





                    if (email == currentuseremail) {

                        mDatabase.child("users").child(username).child("moviesalreadywatched").child(
                            binding.titel.text as String).setValue(binding.titel.text)

                        if(userlikedmovie == true){
                            mDatabase.child("users").child(username).child("moviesUserLiked").child(binding.titel.text as String).setValue(binding.titel.text)

                        }

                    }

                }






            }



        })



        if(userlikedmovie == true){

            val handler = Handler()
            handler.postDelayed({
                checkIfItIsAMatch()
            }, 1000) // Wait 1 second before running checkIfItIsAMatch

        }

        else {
                readMovieData()
        }

    }









}









