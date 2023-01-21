package com.example.moviematcher.navigationbar.matches

import MatchesModel
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.moviematcher.MainViewModel
import com.example.moviematcher.databinding.CardMatchesBinding
import com.example.moviematcher.navigationbar.Movies.MovieDetails
import java.util.EnumSet.of
import java.util.List.of


class MatchesAdapter(private var matches: MutableList<MatchesModel>) :
    RecyclerView.Adapter<MatchesAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardMatchesBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val match = matches[holder.adapterPosition]
        holder.bind(match)

    }

    fun updateData(newMatches: ArrayList<MatchesModel>) {
        this.matches = newMatches
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = matches.size

    class MainHolder(private val binding: CardMatchesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(matches: MatchesModel) {
            binding.moviename.text = matches.moviename
            binding.friendname.text = matches.friends.toString()
            binding.root.setOnClickListener {
               val intent = Intent(it.context, MovieDetails::class.java)
                intent.putExtra("text", matches.moviename)
                it.context.startActivity(intent)

            }

        }
    }
}