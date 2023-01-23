package com.example.moviematcher.navigationbar.friends


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviematcher.FriendMatches
import com.example.moviematcher.databinding.CardFriendsBinding
import com.example.moviematcher.navigationbar.Movies.MovieDetails
import com.github.ajalt.timberkt.Timber
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.NonDisposableHandle.parent


class FriendsAdapter(private val context: Context,private var friends: MutableList<FriendsModel>) :
    RecyclerView.Adapter<FriendsAdapter.MainHolder>() {

    private lateinit var mDatabase: DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardFriendsBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val friend = friends[holder.adapterPosition]
        holder.bind(friend)
        setProfileImage(friend.username.toString(), holder.imageView, context)
    }


    override fun getItemCount(): Int = friends.size

    fun setProfileImage(username: String, imageView: ImageView,context: Context) {
        mDatabase = Firebase.database.reference
        println("Username"+username)
        val rootRef = mDatabase.child("users").child(username)
        rootRef.child("profileImage").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Timber.e(null,{"Failed to get image url from Database"})
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val imageUrl = snapshot.value.toString()
                    println("Image1"+imageUrl)
                    Glide.with(context)
                        .load(imageUrl)
                        .circleCrop()
                        .into(imageView)
                }
            }
        })
    }



    class MainHolder(private val binding: CardFriendsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val imageView = binding.imageView
        fun bind(friends: FriendsModel) {
            binding.username.text = friends.username
            binding.root.setOnClickListener {
                val intent = Intent(itemView.context, FriendMatches::class.java)
                intent.putExtra("friend", friends.username)
                itemView.context.startActivity(intent)
                Timber.i(null, { "Start MovieDetails" })
            }
        }
    }

}
