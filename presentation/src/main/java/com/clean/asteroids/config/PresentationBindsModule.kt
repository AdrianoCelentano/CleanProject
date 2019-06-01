package com.clean.asteroids.config

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.clean.asteroids.AsteroidActivity
import com.clean.asteroids.AsteroidViewModel
import com.clean.asteroids.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(includes = [PresentationBindsModule::class])
object PresentationModule {


}

@Module
interface PresentationBindsModule {

    @Binds
    @IntoMap
    @ClassKey(AsteroidViewModel::class)
    fun bindMainViewModel(asteroidViewModel: AsteroidViewModel): ViewModel

    @Binds
    fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    fun bindActivity(asteroidActivity: AsteroidActivity): Activity
}
