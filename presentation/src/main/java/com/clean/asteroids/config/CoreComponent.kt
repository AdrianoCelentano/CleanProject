package com.clean.asteroids.config

import com.clean.domain.asteroid.usecase.GetAsteroidOfTheDay

interface CoreComponent {

    fun provideGetAsteroidOfTheDay(): GetAsteroidOfTheDay
}