package com.clean.domain.asteroid.usecase

import com.clean.domain.asteroid.NasaRepository
import com.clean.domain.asteroid.StringProvider
import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewResult
import io.reactivex.Observable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import javax.inject.Inject

class GetAsteroidOfTheDay @Inject constructor(
    private val nasaRepository: NasaRepository,
    private val stringProvider: StringProvider
) : AsteroidUseCase<AsteroidViewEvent.Load> {

    override fun isForEvent(event: AsteroidViewEvent): Boolean {
        return event is AsteroidViewEvent.Load
    }

    override suspend fun execute(event: AsteroidViewEvent.Load): ReceiveChannel<AsteroidViewResult> {
        val channel = Channel<AsteroidViewResult>()
        try {
            channel.send(AsteroidViewResult.AsteroidPartialState.Loading)
            val asteroid = loadAsteroid()
            channel.send(asteroid)
        } catch (excpetion: Exception) {
            println(excpetion.message)
            channel.send(AsteroidViewResult.AsteroidPartialState.Error(stringProvider.generalError))
        }
        return channel
    }

    suspend fun loadAsteroid(): AsteroidViewResult {
        val asteroid = nasaRepository.getAsteroidOfTheDay()
        return AsteroidViewResult.AsteroidPartialState.NewAsteroid(asteroid)
    }
}