package com.example.moviematcher.data

data class User(
    val email: String,
    val firstName: String,
    val username: String,
    val friends: ArrayList<String>
)
