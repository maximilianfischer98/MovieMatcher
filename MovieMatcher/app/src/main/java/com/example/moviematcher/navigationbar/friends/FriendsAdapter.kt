package com.example.moviematcher.navigationbar.friends


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviematcher.MainViewModel
import com.example.moviematcher.databinding.CardFriendsBinding
import com.github.ajalt.timberkt.Timber
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


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

    fun updateData(newData: List<FriendsModel>) {
        friends.clear()
        friends.addAll(newData)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        try {
            if (position >= 0 && position < friends.size) {
                friends.removeAt(position)
                notifyItemRemoved(position)
            } else {
                Timber.e(null,{"Invalid position passed to removeItem method"})
            }
        } catch (e: IndexOutOfBoundsException) {
            Timber.e(null,{"IndexOutOfBoundsException occurred: ${e.message}"})
        }
    }

    fun getName(position: Int): String {
        try {
            return friends[position].username.toString()
        } catch (e: IndexOutOfBoundsException) {
            Timber.e(null,{"IndexOutofBoundsException"})
            return "ArrayList is Empty"
        }
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

class SwipeToDeleteCallback(private val adapter: FriendsAdapter, private val viewModel: MainViewModel) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        println("Aktuelle Adapterposition: ${position}")
        println("DeleteName " + adapter.getName(position))
        viewModel.removeFriendDB(adapter.getName(position))
        adapter.removeItem(position)
        //adapter.notifyItemRemoved(position)

    }
}
