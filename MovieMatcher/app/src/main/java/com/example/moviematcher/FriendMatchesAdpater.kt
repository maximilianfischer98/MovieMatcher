package com.example.moviematcher

import android.content.Intent
import com.example.moviematcher.navigationbar.friends.FriendsModel


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviematcher.databinding.CardFriendMatchesBinding
import com.example.moviematcher.databinding.CardFriendsBinding
import com.example.moviematcher.navigationbar.Movies.MovieDetails
import com.github.ajalt.timberkt.Timber


class FriendMatchesAdapter(private var friendMatches: MutableList<FriendMatchesModel>) :
    RecyclerView.Adapter<FriendMatchesAdapter.MainHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardFriendMatchesBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val friendMatches = friendMatches[holder.adapterPosition]
        holder.bind(friendMatches)
    }


    override fun getItemCount(): Int = friendMatches.size


    class MainHolder(private val binding: CardFriendMatchesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(friendMatches: FriendMatchesModel) {
            binding.moviename.text = friendMatches.moviename
            binding.root.setOnClickListener {
                val intent = Intent(it.context, MovieDetails::class.java)
                intent.putExtra("text", friendMatches.moviename)
                it.context.startActivity(intent)
                Timber.i(null, { "Start MovieDetails" })
            }
        }

    }
}