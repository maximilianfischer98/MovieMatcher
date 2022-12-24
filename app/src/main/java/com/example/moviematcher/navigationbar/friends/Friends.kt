
package com.example.moviematcher.navigationbar.friends

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviematcher.MainApp
import com.example.moviematcher.R
import com.example.moviematcher.data.Movie
import com.example.moviematcher.databinding.FragmentFriendsBinding



class Friends: Fragment() {

    private lateinit var binding: FragmentFriendsBinding
    lateinit var app: MainApp
    val movieList= ArrayList<Movie>()
    var hi = 1


    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFriendsBinding.inflate(inflater,container,false)
        binding = FragmentFriendsBinding.inflate(layoutInflater)




        app = requireActivity().application as MainApp




        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = FriendsAdapter(app.friends)

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

                val intent = Intent(activity, addFriend::class.java)
                getResult.launch(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.friends.size)
            }
        }








    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }







}

