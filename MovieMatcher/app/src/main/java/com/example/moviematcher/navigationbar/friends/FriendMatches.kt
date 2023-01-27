package com.example.moviematcher.navigationbar.friends

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviematcher.MainViewModel
import com.example.moviematcher.R
import com.example.moviematcher.databinding.FriendMatchesBinding
import com.example.moviematcher.navigationbar.NavigationController
import com.github.ajalt.timberkt.Timber


class FriendMatches : AppCompatActivity() {
    private lateinit var binding: FriendMatchesBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FriendMatchesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val friendName = intent.getStringExtra("friend")
        val title = getString(R.string.matches_with_friend) + " " + friendName
        binding.title.text = title

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.loadMatches()
        viewModel.matches.observe(
            this,
            Observer {
                if (it.isNotEmpty()) {

                    viewModel.filterMoviesOfFriend(friendName!!, viewModel.matches.value!!)

                }
                Timber.i(null, { "Matches List empty" })
            }
        )





        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        viewModel.friendMatches.observe(
            this,
            Observer {
                if (it.isNotEmpty()) {
                    binding.recyclerView.adapter = FriendMatchesAdapter(it)
                } else {
                    Timber.i(null, { "Friend Matches List empty" })
                }
            }
        )
    }


}
