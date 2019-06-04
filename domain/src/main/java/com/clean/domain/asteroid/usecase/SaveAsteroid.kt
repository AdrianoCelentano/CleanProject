package com.clean.domain.asteroid.usecase

import com.clean.domain.asteroid.NasaRepository
import com.clean.domain.asteroid.StringProvider
import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewResult
import io.reactivex.Observable
import javax.inject.Inject

class SaveAsteroid @Inject constructor(
    private val nasaRepository: NasaRepository,
    private val stringProvider: StringProvider
) : AsteroidUseCase<AsteroidViewEvent.Store> {

    override fun isForEvent(event: AsteroidViewEvent): Boolean {
        return event is AsteroidViewEvent.Store
    }

    override fun execute(event: AsteroidViewEvent.Store): Observable<AsteroidViewResult> {
        return Observable.concat(saveAsteroid(event), emitUserMessageEffect())
            .onErrorReturnItem(AsteroidViewResult.AsteroidPartialState.Error(stringProvider.generalError))
            .doOnError { println(it.message) }
    }

    private fun saveAsteroid(event: AsteroidViewEvent.Store): Observable<AsteroidViewResult> {
        return nasaRepository.saveAsteroid(event.asteroid).toObservable()
    }

    private fun emitUserMessageEffect(): Observable<AsteroidViewResult> {
        return Observable.just(AsteroidViewResult.AsteroidViewEffect.UserMessage(stringProvider.storeAsteroidSuccess))
    }
}