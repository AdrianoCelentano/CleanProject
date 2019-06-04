package com.clean.asteroids.config

import com.clean.domain.asteroid.AsteroidViewFlow

interface CoreComponent {

    fun provideAsteriodFlow(): AsteroidViewFlow
}