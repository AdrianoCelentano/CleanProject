package com.clean.asteroids

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.clean.domain.asteroid.AsteroidViewFlow
import com.clean.domain.asteroid.model.Asteroid
import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewResult
import com.clean.domain.asteroid.model.AsteroidViewState
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AsteroidViewModel @Inject constructor(
    asteroidViewFlow: AsteroidViewFlow
) : ViewModel() {

    val viewStateLive
        get() = viewStateMutableLive

    val viewEffectEmitter: Observable<AsteroidViewResult.AsteroidViewEffect>

    val asteroidOfTheDay: Asteroid? get() = viewStateLive.value?.data?.asteroid

    private val viewStateMutableLive by lazy { MutableLiveData<AsteroidViewState>() }

    private val eventRelay: PublishRelay<AsteroidViewEvent> = PublishRelay.create()

    private val eventEmitter get() = eventRelay.hide().startWith(AsteroidViewEvent.Load)

    private val disposables: CompositeDisposable = CompositeDisposable()

    init {
        val (effectEmitter, viewStateEmitter) =
            asteroidViewFlow.start(eventEmitter)

        observeViewState(viewStateEmitter)

        this.viewEffectEmitter = effectEmitter
    }

    fun processEvent(viewEvent: AsteroidViewEvent) {
        eventRelay.accept(viewEvent)
    }

    private fun observeViewState(viewStateEmitter: Observable<AsteroidViewState>) {
        viewStateEmitter
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = { viewState -> viewStateLive.postValue(viewState) },
                onError = { error -> Log.e("qwer", "error viewstate", error) }
            ).addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}