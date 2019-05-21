package com.clean.asteroids.mapper

import com.clean.asteroids.model.Asteroid as PresentationAsteroid
import com.clean.domain.Asteroid as DomainAsteroid
import javax.inject.Inject

class AsteroidMapper @Inject constructor() {

    fun map(domainAsteroid: DomainAsteroid): PresentationAsteroid {
        return PresentationAsteroid(
            name = domainAsteroid.title,
            imageUrl = domainAsteroid.url
        )
    }
}