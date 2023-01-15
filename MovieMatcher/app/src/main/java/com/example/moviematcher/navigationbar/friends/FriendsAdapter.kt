package com.example.moviematcher.navigationbar.friends

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.constraintlayout.motion.widget.KeyFrames
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.moviematcher.R
import com.example.moviematcher.databinding.CardFriendsBinding



class FriendsAdapter(private var friends: MutableList<FriendsModel>) :
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




    fun updateData(newData: ArrayList<FriendsModel>) {
        friends = newData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = friends.size




    class MainHolder(private val binding: CardFriendsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(friends: FriendsModel) {
            binding.username.text = friends.username

        }
    }

}
