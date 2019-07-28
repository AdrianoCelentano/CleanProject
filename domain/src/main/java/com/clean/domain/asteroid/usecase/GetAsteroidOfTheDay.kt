package com.clean.domain.asteroid.usecase

import com.clean.domain.asteroid.NasaRepository
import com.clean.domain.asteroid.StringProvider
import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewResult
import kotlinx.coroutines.channels.SendChannel
import javax.inject.Inject

class GetAsteroidOfTheDay @Inject constructor(
    private val nasaRepository: NasaRepository,
    private val stringProvider: StringProvider
) : AsteroidUseCase<AsteroidViewEvent.Load> {

    override fun isForEvent(event: AsteroidViewEvent): Boolean {
        return event is AsteroidViewEvent.Load
    }

    override suspend fun execute(
        event: AsteroidViewEvent.Load,
        resultChannel: SendChannel<AsteroidViewResult>
    ) {
        try {
            resultChannel.send(AsteroidViewResult.AsteroidPartialState.Loading)
            val asteroid = loadAsteroid()
            resultChannel.send(asteroid)
        } catch (excpetion: Exception) {
            println(excpetion.message)
            resultChannel.send(AsteroidViewResult.AsteroidPartialState.Error(stringProvider.generalError))
        }
    }

    suspend fun loadAsteroid(): AsteroidViewResult {
        val asteroid = nasaRepository.getAsteroidOfTheDay()
        return AsteroidViewResult.AsteroidPartialState.NewAsteroid(asteroid)
    }
}