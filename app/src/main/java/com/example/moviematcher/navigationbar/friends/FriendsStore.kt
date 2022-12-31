package com.example.moviematcher.navigationbar.friends

interface FriendsStore {
    fun findAll(): List<FriendsModel>
    fun create(placemark: FriendsModel)
    fun update(placemark: FriendsModel)
}