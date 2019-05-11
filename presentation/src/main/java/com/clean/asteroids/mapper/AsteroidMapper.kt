package com.clean.asteroids.mapper

import com.clean.asteroids.model.Asteroid
import javax.inject.Inject

class AsteroidMapper @Inject constructor() {

    fun map(domain: com.clean.domain.Asteroid): Asteroid {
        return Asteroid(
            name = domain.title,
            imageUrl = domain.url
        )
    }
}