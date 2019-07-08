package com.clean.data

import com.clean.data.cache.NasaCache
import com.clean.data.mapper.AsteroidMapper
import com.clean.data.remote.NasaRemote
import com.clean.domain.asteroid.NasaRepository
import com.clean.domain.asteroid.model.Asteroid
import javax.inject.Inject

class NasaRepositoryImpl @Inject constructor(
    val nasaRemote: NasaRemote,
    val nasaCache: NasaCache,
    private val asteroidMapper: AsteroidMapper
) : NasaRepository {

    override suspend fun getAsteroidOfTheDay(): Asteroid {
        val asteroid = nasaRemote.getAsteroidOfTheDay()
        return asteroidMapper.mapDataToDomain(asteroid)
    }

    override suspend fun saveAsteroid(asteroid: Asteroid) {
        val domainAsteroid = asteroidMapper.mapDomaintoEntity(asteroid)
        nasaCache.saveAsteroid(domainAsteroid)
    }

    override suspend fun getSavedAsteroid(): List<Asteroid> {
        return nasaCache.getAsteroids()
            .map { asteroid -> asteroidMapper.mapEntitiyToDomain(asteroid) }
    }
}