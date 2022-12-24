
package com.example.moviematcher.navigationbar.matches

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviematcher.MainApp
import com.example.moviematcher.R
import com.example.moviematcher.databinding.FragmentMatchesBinding



class Matches: Fragment() {

    private lateinit var binding: FragmentMatchesBinding
    lateinit var app: MainApp





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {



        binding = FragmentMatchesBinding.inflate(inflater,container,false)
        binding = FragmentMatchesBinding.inflate(layoutInflater)








        app = requireActivity().application as MainApp




        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = MatchesAdapter(app.matches)


        (activity as AppCompatActivity).supportActionBar
        setHasOptionsMenu(true);





        return binding.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {



       // binding.spin.adapter = ArrayAdapter.createFromResource(
          //  requireContext(), R.array.friends_array, android.R.layout.simple_spinner_item)


    }











}

