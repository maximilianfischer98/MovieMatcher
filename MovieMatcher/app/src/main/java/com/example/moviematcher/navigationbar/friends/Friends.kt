
package com.example.moviematcher.navigationbar.friends

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviematcher.MainApp
import com.example.moviematcher.MainViewModel
import com.example.moviematcher.R
import com.example.moviematcher.databinding.FragmentFriendsBinding


class Friends: Fragment() {


    private lateinit var binding: FragmentFriendsBinding
    lateinit var app: MainApp


    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

       val viewModel : MainViewModel by activityViewModels()
        binding = FragmentFriendsBinding.inflate(inflater,container,false)
        binding = FragmentFriendsBinding.inflate(layoutInflater)
        viewModel.getCurrentUserMail()?.let { viewModel.loadFriends(it) }

        app = requireActivity().application as MainApp




        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager


        viewModel.friends.observe(
            viewLifecycleOwner,
            Observer {
                if (it.isNotEmpty()) {
                    binding.recyclerView.adapter = FriendsAdapter(it)

                }
            }
        )

        (activity as AppCompatActivity).supportActionBar
        setHasOptionsMenu(true);


        return binding.root


    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.friends_menu, menu)
        super.onCreateOptionsMenu(menu!!, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {

                findNavController().navigate(R.id.addFriendFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {

            }
        }








    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }









}

