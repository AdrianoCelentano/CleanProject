package com.clean.data

import com.clean.data.mapper.EntityMapper
import com.clean.data.remote.NasaRemote
import com.clean.data.remote.model.Asteroid
import com.clean.domain.NasaRepository
import io.reactivex.Single
import javax.inject.Inject

class NasaRepositoryImpl @Inject constructor(
    private val nasaRemote: NasaRemote,
    private val mapper: EntityMapper<Asteroid, com.clean.domain.Asteroid>
) : NasaRepository {

    override fun getAsteroidOfTheDay(): Single<com.clean.domain.Asteroid> {
        return nasaRemote.getAsteroidOfTheDay()
            .map { mapper.mapToEntity(it) }
    }

}