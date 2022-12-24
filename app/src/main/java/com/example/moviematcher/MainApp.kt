package com.example.moviematcher

import MatchesModel
import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.moviematcher.choosecategory.Categorys
import com.example.moviematcher.choosecategory.ChooseCategoryModel
import com.example.moviematcher.data.Movie
import com.example.moviematcher.login.LoginScreen
import com.example.moviematcher.navigationbar.friends.FriendsModel


class MainApp : Application() {

    val categorys = ArrayList<ChooseCategoryModel>()
    val friends = ArrayList<FriendsModel>()
    val matches = ArrayList<MatchesModel>()





    override fun onCreate() {
        super.onCreate()

        categorys.add(ChooseCategoryModel(Categorys.Action.toString()));
        categorys.add(ChooseCategoryModel(Categorys.Comedy.toString()));
        categorys.add(ChooseCategoryModel(Categorys.Drama.toString()));
        categorys.add(ChooseCategoryModel(Categorys.Horror.toString()));



        friends.add(FriendsModel("test2", "luke"))
        friends.add(FriendsModel("masd123", "Ben"))
        friends.add(FriendsModel("gay123", "fay"))
        friends.add(FriendsModel("lsaf3", "mare"))


        matches.add(MatchesModel("movies", "max"))
        matches.add(MatchesModel("movie2", "luke"))


    }





}