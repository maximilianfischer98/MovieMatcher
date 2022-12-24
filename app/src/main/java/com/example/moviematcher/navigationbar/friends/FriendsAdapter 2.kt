package com.example.moviematcher.navigationbar.friends

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviematcher.databinding.CardFriendsBinding


class FriendsAdapter(private var friends: ArrayList<FriendsModel>) :
    RecyclerView.Adapter<FriendsAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardFriendsBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val friend = friends[holder.adapterPosition]
        holder.bind(friend)
    }

    override fun getItemCount(): Int = friends.size

    class MainHolder(private val binding: CardFriendsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(friends: FriendsModel) {
            binding.username.text = friends.username
            binding.realname.text = friends.realname

        }
    }
}