package com.clean.asteroids.config

import com.clean.asteroids.AsteroidActivity
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = arrayOf(PresentationModule::class),
    dependencies = arrayOf(CoreComponent::class)
)
interface PresentationComponent {

    fun inject(asteroidActivity: AsteroidActivity)

    @Component.Builder
    interface Builder {

        fun coreComponent(coreComponent: CoreComponent): Builder
        @BindsInstance
        fun activity(asteroidActivity: AsteroidActivity): Builder


        fun build(): PresentationComponent
    }
}