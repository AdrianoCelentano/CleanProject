package com.clean.domain.asteroid.usecase

import com.clean.domain.asteroid.NasaRepository
import com.clean.domain.asteroid.StringProvider
import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewResult
import io.reactivex.Observable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import javax.inject.Inject

class SaveAsteroid @Inject constructor(
    private val nasaRepository: NasaRepository,
    private val stringProvider: StringProvider
) : AsteroidUseCase<AsteroidViewEvent.Store> {

    override fun isForEvent(event: AsteroidViewEvent): Boolean {
        return event is AsteroidViewEvent.Store
    }

    override suspend fun execute(event: AsteroidViewEvent.Store): ReceiveChannel<AsteroidViewResult> {

        val channel = Channel<AsteroidViewResult>()
        try {
            saveAsteroid(event)
            channel.send(userMessageEffect())
        } catch (excpetion: Exception) {
            println(excpetion.message)
            channel.send(AsteroidViewResult.AsteroidPartialState.Error(stringProvider.generalError))
        }
        return channel
    }

    private suspend fun saveAsteroid(event: AsteroidViewEvent.Store) {
        return nasaRepository.saveAsteroid(event.asteroid)
    }

    private fun userMessageEffect(): AsteroidViewResult {
        return AsteroidViewResult.AsteroidViewEffect.UserMessage(stringProvider.storeAsteroidSuccess)
    }
}