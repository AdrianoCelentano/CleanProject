package com.clean.project

import com.clean.asteroids.config.CoreComponent
import com.clean.data.config.DataComponent
import com.clean.domain.asteroid.usecase.GetAsteroidOfTheDay
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = arrayOf(AppModule::class),
    dependencies = arrayOf(DataComponent::class)
)
interface AppComponent : CoreComponent {

    override fun provideGetAsteroidOfTheDay(): GetAsteroidOfTheDay

    @Component.Builder
    interface Builder {

        fun dataComponent(dataComponent: DataComponent): Builder
        fun build(): AppComponent
    }
}