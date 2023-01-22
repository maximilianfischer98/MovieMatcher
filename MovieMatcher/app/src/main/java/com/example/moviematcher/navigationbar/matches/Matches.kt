
package com.example.moviematcher.navigationbar.matches

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviematcher.MainViewModel
import com.example.moviematcher.databinding.FragmentMatchesBinding
import com.github.ajalt.timberkt.Timber


class Matches: Fragment() {

    private lateinit var binding: FragmentMatchesBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {



        binding = FragmentMatchesBinding.inflate(inflater,container,false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel : MainViewModel by activityViewModels()
        viewModel.loadMatches()


        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager


        viewModel.matches.observe(
            viewLifecycleOwner,
            Observer {
                if (it.isNotEmpty()) {
                    binding.recyclerView.adapter = MatchesAdapter(it)

                }
                Timber.i(null, { "Matches List empty" })
            }
        )


    }

















}

