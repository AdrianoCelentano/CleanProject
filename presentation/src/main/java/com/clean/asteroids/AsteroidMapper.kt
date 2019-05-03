package com.clean.asteroids

import javax.inject.Inject

class AsteroidMapper @Inject constructor() {

    fun map(domain: com.clean.domain.Asteroid): Asteroid {
        return Asteroid(
            name = domain.title,
            imageUrl = domain.url
        )
    }
}