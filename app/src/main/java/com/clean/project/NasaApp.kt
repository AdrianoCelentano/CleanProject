package com.clean.project

import android.app.Activity
import android.app.Application
import com.clean.asteroids.ActivityInjector
import com.clean.asteroids.MainActivity
import com.clean.data.config.DaggerDataComponent

class NasaApp : Application(), ActivityInjector {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .dataComponent(DaggerDataComponent.builder().build())
            .application(this)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun inject(activity: Activity) {
        when (activity) {
            is MainActivity -> appComponent.inject(activity)
        }
    }
}