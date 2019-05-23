package com.clean.data

import com.clean.data.mapper.AsteroidMapper
import com.clean.data.remote.NasaRemote
import com.clean.domain.Asteroid
import com.clean.domain.NasaRepository
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class NasaRepositoryImpl @Inject constructor(
    val nasaRemote: NasaRemote,
    private val asteroidMapper: AsteroidMapper
) : NasaRepository {

    override fun getAsteroidOfTheDay(): Observable<Asteroid> {
        return nasaRemote.getAsteroidOfTheDay().map { asteroidMapper.map(it) }
    }
}