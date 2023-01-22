package com.example.moviematcher



import android.app.Application

import com.google.firebase.FirebaseApp

import timber.log.Timber


class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        FirebaseApp.initializeApp(this)


    }




}