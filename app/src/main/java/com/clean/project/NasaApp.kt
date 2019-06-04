package com.clean.project

import android.app.Application
import com.clean.asteroids.config.CoreComponent
import com.clean.asteroids.config.CoreComponentProvider
import com.clean.data.config.DaggerDataComponent
import com.facebook.stetho.Stetho

class NasaApp : Application(), CoreComponentProvider {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .dataComponent(dataComponent())
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this);
    }

    override fun provide(): CoreComponent {
        return appComponent
    }

    private fun dataComponent() = DaggerDataComponent.builder().application(this).build()
}