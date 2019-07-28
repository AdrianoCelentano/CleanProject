package com.clean.domain.asteroid.usecase

import com.clean.domain.asteroid.NasaRepository
import com.clean.domain.asteroid.StringProvider
import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewResult
import kotlinx.coroutines.channels.SendChannel
import javax.inject.Inject

class SaveAsteroid @Inject constructor(
    private val nasaRepository: NasaRepository,
    private val stringProvider: StringProvider
) : AsteroidUseCase<AsteroidViewEvent.Store> {

    override fun isForEvent(event: AsteroidViewEvent): Boolean {
        return event is AsteroidViewEvent.Store
    }

    override suspend fun execute(
        event: AsteroidViewEvent.Store,
        resultChannel: SendChannel<AsteroidViewResult>
    ) {

        try {
            saveAsteroid(event)
            resultChannel.send(userMessageEffect())
        } catch (excpetion: Exception) {
            println(excpetion.message)
            resultChannel.send(AsteroidViewResult.AsteroidPartialState.Error(stringProvider.generalError))
        }
    }

    private suspend fun saveAsteroid(event: AsteroidViewEvent.Store) {
        return nasaRepository.saveAsteroid(event.asteroid)
    }

    private fun userMessageEffect(): AsteroidViewResult {
        return AsteroidViewResult.AsteroidViewEffect.UserMessage(stringProvider.storeAsteroidSuccess)
    }
}