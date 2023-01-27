
package com.example.moviematcher.navigationbar.friends


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.moviematcher.MainViewModel
import com.example.moviematcher.R
import com.example.moviematcher.databinding.FragmentFriendsBinding

import com.github.ajalt.timberkt.Timber



class Friends: Fragment() {


    private lateinit var binding: FragmentFriendsBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFriendsBinding.inflate(inflater,container,false)
        binding = FragmentFriendsBinding.inflate(layoutInflater)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewModel : MainViewModel by activityViewModels()

        viewModel.getCurrentUserMail()?.let { viewModel.loadFriends(it) }
        println("reload")

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager



        viewModel.friends.observe(
            viewLifecycleOwner,
            Observer {
                if (it.isNotEmpty()) {
                    println("123kk")
                    val nonEmptyFriends = mutableListOf<FriendsModel>()
                    for (i in it.indices) {
                        if (it[i].username != "") {
                            nonEmptyFriends.add(it[i])
                        }
                    }
                    val adapter = FriendsAdapter(requireContext(),nonEmptyFriends)
                    val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter,viewModel))
                    itemTouchHelper.attachToRecyclerView(binding.recyclerView)
                    binding.recyclerView.adapter = adapter




                }
                Timber.i(null, { "Friends List empty" })
            }
        )

        binding.AddButton.setOnClickListener(){
            findNavController().navigate(R.id.addFriendFragment)
            Timber.i(null, { "Start add Friend Fragment" })
        }


    }












}

