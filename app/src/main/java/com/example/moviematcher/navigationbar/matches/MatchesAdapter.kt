package com.example.moviematcher.navigationbar.matches

import MatchesModel
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviematcher.databinding.CardMatchesBinding


class MatchesAdapter(private var matches: ArrayList<MatchesModel>) :
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

    override fun getItemCount(): Int = matches.size

    class MainHolder(private val binding: CardMatchesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(matches: MatchesModel) {
            binding.moviename.text = matches.moviename
            binding.friendname.text = matches.friend

        }
    }
}