package com.example.moviematcher.data

import com.example.moviematcher.R

data class User(
    val email: String,
    val username: String,
    val firstName: String,
    val friends: ArrayList<String> = arrayListOf()
)
