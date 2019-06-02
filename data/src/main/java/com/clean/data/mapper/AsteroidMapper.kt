package com.clean.data.mapper

import javax.inject.Inject
import com.clean.data.model.Asteroid as DataAsteroid
import com.clean.domain.asteroid.model.Asteroid as DomainAsteroid

class AsteroidMapper @Inject constructor() {

    fun map(dataAsteroid: DataAsteroid): DomainAsteroid {
        return DomainAsteroid(
            title = dataAsteroid.title,
            imageUrl = dataAsteroid.url
        )
    }
}