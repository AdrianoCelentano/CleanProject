package com.clean.data

import com.clean.data.mapper.AsteroidMapper
import com.clean.data.remote.NasaRemote
import com.clean.domain.asteroid.NasaRepository
import com.clean.domain.asteroid.model.Asteroid
import io.reactivex.Observable
import javax.inject.Inject

class NasaRepositoryImpl @Inject constructor(
    val nasaRemote: NasaRemote,
    private val asteroidMapper: AsteroidMapper
) : NasaRepository {

    override fun getAsteroidOfTheDay(): Observable<Asteroid> {
        return nasaRemote.getAsteroidOfTheDay().map { asteroidMapper.map(it) }
    }
}