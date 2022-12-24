package com.example.moviematcher.choosecategory

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviematcher.MainApp
import com.example.moviematcher.databinding.ActivityChooseCategoryListBinding
import com.example.moviematcher.navigationbar.NavigationController


class ChooseCategoryList : AppCompatActivity()  {

    lateinit var app: MainApp
    private lateinit var binding: ActivityChooseCategoryListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseCategoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = ChooseCategoryAdapter(app.categorys)

        binding.buttonNext.setOnClickListener{
            val intent = Intent(this, NavigationController::class.java)
            startActivity(intent)
        }
    }




}