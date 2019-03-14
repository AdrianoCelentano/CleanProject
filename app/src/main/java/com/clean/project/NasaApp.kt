package com.clean.project

import android.app.Application

class NasaApp : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .application(this)
            .build()
    }

    override fun onCreate() {
        super.onCreate()

    }
}