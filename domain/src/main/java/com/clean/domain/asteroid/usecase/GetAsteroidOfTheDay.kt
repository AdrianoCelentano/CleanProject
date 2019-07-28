package com.clean.domain.asteroid.usecase

import com.clean.domain.asteroid.NasaRepository
import com.clean.domain.asteroid.StringProvider
import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAsteroidOfTheDay @Inject constructor(
    private val nasaRepository: NasaRepository,
    private val stringProvider: StringProvider
) : AsteroidUseCase<AsteroidViewEvent.Load> {

    override fun isForEvent(event: AsteroidViewEvent): Boolean {
        return event is AsteroidViewEvent.Load
    }

    override suspend fun execute(
        event: AsteroidViewEvent.Load
    ): Flow<AsteroidViewResult> {
        return flow {
            try {
                emit(AsteroidViewResult.AsteroidPartialState.Loading)
                val asteroid = loadAsteroid()
                emit(asteroid)
            } catch (excpetion: Exception) {
                println(excpetion.message)
                emit(AsteroidViewResult.AsteroidPartialState.Error(stringProvider.generalError))
            }
        }
    }

    suspend fun loadAsteroid(): AsteroidViewResult {
        val asteroid = nasaRepository.getAsteroidOfTheDay()
        return AsteroidViewResult.AsteroidPartialState.NewAsteroid(asteroid)
    }
}