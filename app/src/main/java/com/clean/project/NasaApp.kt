package com.clean.project

import android.app.Application
import com.clean.asteroids.config.CoreComponent
import com.clean.asteroids.config.CoreComponentProvider
import com.clean.data.config.DaggerDataComponent

class NasaApp : Application(), CoreComponentProvider {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .dataComponent(DaggerDataComponent.builder().build())
            .application(this)
            .build()
    }

    override fun provide(): CoreComponent {
        return appComponent
    }
}