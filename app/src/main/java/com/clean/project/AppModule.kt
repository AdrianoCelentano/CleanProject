package com.clean.project

import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.usecase.AsteroidUseCase
import com.clean.domain.asteroid.usecase.GetAsteroidOfTheDay
import com.clean.domain.asteroid.usecase.SaveAsteroid
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module
object AppModule {

    @Provides
    @JvmStatic
    @IntoSet
    fun provideGetAsteroidOfTheDayUseCase(getAsteroidOfTheDay: GetAsteroidOfTheDay): AsteroidUseCase<AsteroidViewEvent> {
        return getAsteroidOfTheDay as AsteroidUseCase<AsteroidViewEvent>
    }

    @Provides
    @JvmStatic
    @IntoSet
    fun provideSaveAsteroid(saveAsteroid: SaveAsteroid): AsteroidUseCase<AsteroidViewEvent> {
        return saveAsteroid as AsteroidUseCase<AsteroidViewEvent>
    }

}
