package com.clean.data.mapper

import com.clean.data.cache.AsteroidEntity
import javax.inject.Inject
import com.clean.data.model.Asteroid as DataAsteroid
import com.clean.domain.asteroid.model.Asteroid as DomainAsteroid

class AsteroidMapper @Inject constructor() {

    fun mapDataToDomain(dataAsteroid: DataAsteroid): DomainAsteroid {
        return DomainAsteroid(
            title = dataAsteroid.title,
            imageUrl = dataAsteroid.url
        )
    }

    fun mapDomaintoEntity(domainAsteroid: DomainAsteroid): AsteroidEntity {
        return AsteroidEntity(
            title = domainAsteroid.title,
            url = domainAsteroid.imageUrl
        )
    }

    fun mapEntitiyToDomain(asteroidEntity: AsteroidEntity): DomainAsteroid {
        return DomainAsteroid(
            title = asteroidEntity.title,
            imageUrl = asteroidEntity.url
        )
    }
}