package com.clean.project

import android.app.Application
import com.clean.asteroids.config.CoreComponent
import com.clean.asteroids.config.CoreComponentProvider
import com.clean.data.config.DaggerDataComponent

class NasaApp : Application(), CoreComponentProvider {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .dataComponent(dataComponent())
            .build()
    }

    private fun dataComponent() = DaggerDataComponent.builder().application(this).build()

    override fun provide(): CoreComponent {
        return appComponent
    }
}