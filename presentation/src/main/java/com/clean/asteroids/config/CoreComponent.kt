package com.clean.asteroids.config

import com.clean.domain.GetAsteroidOfTheDay

interface CoreComponent {

    fun provideGetAsteroidOfTheDay(): GetAsteroidOfTheDay
}