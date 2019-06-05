package com.clean.data

import com.clean.data.cache.NasaCache
import com.clean.data.mapper.AsteroidMapper
import com.clean.data.remote.NasaRemote
import com.clean.domain.asteroid.NasaRepository
import com.clean.domain.asteroid.model.Asteroid
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class NasaRepositoryImpl @Inject constructor(
    val nasaRemote: NasaRemote,
    val nasaCache: NasaCache,
    private val asteroidMapper: AsteroidMapper
) : NasaRepository {

    override fun getAsteroidOfTheDay(): Observable<Asteroid> {
        return nasaRemote.getAsteroidOfTheDay().map { asteroidMapper.mapDataToDomain(it) }
    }

    override fun saveAsteroid(asteroid: Asteroid): Completable {
        return Observable.just(asteroid)
            .map { asteroidMapper.mapDomaintoEntity(asteroid) }
            .flatMapCompletable { nasaCache.saveAsteroid(it) }
    }

    override fun getSavedAsteroid(): Observable<List<Asteroid>> {
        return nasaCache.getAsteroids()
            .map { asteroids -> asteroids.map { asteroidMapper.mapEntitiyToDomain(it) } }
    }
}