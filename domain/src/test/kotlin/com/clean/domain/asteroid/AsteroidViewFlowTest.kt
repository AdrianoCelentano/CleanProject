package com.clean.domain.asteroid

import com.clean.domain.asteroid.model.Asteroid
import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewResult
import com.clean.domain.asteroid.model.AsteroidViewState
import com.clean.domain.asteroid.usecase.AsteroidUseCase
import com.clean.domain.asteroid.usecase.GetAsteroidOfTheDay
import com.clean.domain.asteroid.usecase.SaveAsteroid
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.assertj.core.api.Assertions
import org.junit.Test

class AsteroidViewFlowTest {

    private val repository: NasaRepository = NasaRepositoryFake()

    private val stringProvider: StringProvider = StringProviderFake()

    private val asteroidViewFlow: AsteroidViewFlow = asteroidViewFlow()

    @Test
    fun `when event is store then return effect user message`() {
        val (effectsEmiter, viewstateEmiter) =
            asteroidViewFlow.start(Observable.just(AsteroidViewEvent.Store(asteroid())))

        emitsUserMessageEffect(effectsEmiter)
    }

    @Test
    fun `when event is store then save Asteroid`() {
        val (effectsEmiter, viewstateEmiter) =
            asteroidViewFlow.start(Observable.just(AsteroidViewEvent.Store(asteroid())))

        val viewStateSubscriber = viewstateEmiter.test()

        asteroidIsStored(asteroid())
    }

    @Test
    fun `when event is store then viewState is not changing`() {
        val viewEventEmiter = PublishSubject.create<AsteroidViewEvent>()
        val (effectsEmiter, viewstateEmiter) =
            asteroidViewFlow.start(viewEventEmiter)

        val viewStateTestSubscriber = viewstateEmiter.test()
        viewEventEmiter.onNext(AsteroidViewEvent.Load)
        val oldItemCount = viewStateTestSubscriber.valueCount()
        viewEventEmiter.onNext(AsteroidViewEvent.Store(asteroid()))
        val newItemCount = viewStateTestSubscriber.valueCount()
        Assertions.assertThat(oldItemCount).isEqualTo(newItemCount)
    }

    @Test
    fun `when event is load then return effect user message`() {
        val (effectsEmiter, viewstateEmiter) =
            asteroidViewFlow.start(Observable.just(AsteroidViewEvent.Load))

        emitsNoEffects(effectsEmiter)
    }

    @Test
    fun `when event is load then return partial states init, loading, data`() {
        val (effectsEmiter, viewstate) =
            asteroidViewFlow.start(Observable.just(AsteroidViewEvent.Load))

        val viewStateSubscriber = viewstate.test()
        viewStateSubscriber.assertValueCount(3)
        viewStateSubscriber.assertValueAt(0, AsteroidViewState.init())
        viewStateSubscriber.assertValueAt(1, { asteroidViewState -> isLoading(asteroidViewState) })
        viewStateSubscriber.assertValueAt(2, { asteroidViewState -> hasData(asteroidViewState) })
    }

    @Test
    fun `when event is second load then return partial states loading, data`() {
        val (effectsEmiter, viewstate) =
            asteroidViewFlow.start(Observable.just(AsteroidViewEvent.Load, AsteroidViewEvent.Load))

        val viewStateSubscriber = viewstate.test()
        viewStateSubscriber.assertValueCount(5)
        viewStateSubscriber.assertValueAt(3, { asteroidViewState -> isLoading(asteroidViewState) })
        viewStateSubscriber.assertValueAt(4, { asteroidViewState -> hasData(asteroidViewState) })
    }

    private fun asteroid() = Asteroid("title", "url")

    private fun asteroidIsStored(asteroid: Asteroid) {
        val savedAsteroids = repository.getSavedAsteroid().test()
        savedAsteroids.assertValueAt(0, { asteroids -> asteroids.contains(asteroid) })
    }

    private fun emitsUserMessageEffect(effects: Observable<AsteroidViewResult.AsteroidViewEffect>) {
        val effectsSubscriber = effects.test()
        effectsSubscriber.assertValueCount(1)
            .assertValue(AsteroidViewResult.AsteroidViewEffect.UserMessage(stringProvider.storeAsteroidSuccess))
    }

    private fun emitsNoEffects(effects: Observable<AsteroidViewResult.AsteroidViewEffect>) {
        val effectsSubscriber = effects.test()
        effectsSubscriber.assertValueCount(0)
    }

    private fun isLoading(asteroidViewState: AsteroidViewState): Boolean =
        asteroidViewState.loading == true && asteroidViewState.errorMessage == null && asteroidViewState.data == null

    private fun hasData(asteroidViewState: AsteroidViewState) =
        asteroidViewState.loading == false && asteroidViewState.errorMessage == null && asteroidViewState.data != null

    private fun asteroidViewFlow(): AsteroidViewFlow {

        return AsteroidViewFlow(
            AsteroidViewEventHandler(
                setOf(
                    GetAsteroidOfTheDay(
                        nasaRepository = repository,
                        stringProvider = stringProvider
                    ) as AsteroidUseCase<AsteroidViewEvent>,
                    SaveAsteroid(
                        nasaRepository = repository,
                        stringProvider = stringProvider
                    ) as AsteroidUseCase<AsteroidViewEvent>
                )
            ),
            AsteroidViewStateReducer()
        )
    }
}