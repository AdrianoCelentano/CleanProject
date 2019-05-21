package com.clean.data.mapper

import com.clean.data.model.Asteroid as DataAsteroid
import com.clean.domain.Asteroid as DomainAsteroid
import javax.inject.Inject

class AsteroidMapper @Inject constructor() {

    fun map(dataAsteroid: DataAsteroid): DomainAsteroid {
        return DomainAsteroid(
            copyright = dataAsteroid.copyright,
            date = dataAsteroid.date,
            explanation = dataAsteroid.explanation,
            hdurl = dataAsteroid.hdurl,
            media_type = dataAsteroid.media_type,
            service_version = dataAsteroid.service_version,
            title = dataAsteroid.title,
            url = dataAsteroid.url
        )
    }
}