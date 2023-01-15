
package com.example.moviematcher.navigationbar.matches

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviematcher.MainApp
import com.example.moviematcher.MainViewModel
import com.example.moviematcher.databinding.FragmentMatchesBinding


class Matches: Fragment() {

    private lateinit var binding: FragmentMatchesBinding
    lateinit var app: MainApp





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewModel : MainViewModel by activityViewModels()

        binding = FragmentMatchesBinding.inflate(inflater,container,false)
        binding = FragmentMatchesBinding.inflate(layoutInflater)
        viewModel.loadMatches()

        app = requireActivity().application as MainApp

        //app.addMatches()

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager


        viewModel.matches.observe(
            viewLifecycleOwner,
            Observer {
                if (it.isNotEmpty()) {
                    binding.recyclerView.adapter = MatchesAdapter(it)

                }
            }
        )


        (activity as AppCompatActivity).supportActionBar
        setHasOptionsMenu(true);





        return binding.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {




    }













}

