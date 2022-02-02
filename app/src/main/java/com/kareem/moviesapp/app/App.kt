package com.kareem.moviesapp.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {
    companion object {
        lateinit var instance: App
            private set
        const val Img_Suffix="https://image.tmdb.org/t/p/original/"
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

    }
}