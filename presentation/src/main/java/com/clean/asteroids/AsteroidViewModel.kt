package com.clean.asteroids

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.clean.domain.asteroid.AsteroidViewFlow
import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewResult
import com.clean.domain.asteroid.model.AsteroidViewState
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AsteroidViewModel @Inject constructor(
    private val asteroidViewFlow: AsteroidViewFlow
) : ViewModel() {

    val viewStateLive
        get() = viewStateMutableLive

    val viewEffectLive
        get() = viewEffectMutableLive

    private val viewStateMutableLive by lazy { MutableLiveData<AsteroidViewState>() }

    private val viewEffectMutableLive by lazy { MutableLiveData<AsteroidViewResult.AsteroidViewEffect>() }

    private val eventRelay: PublishRelay<AsteroidViewEvent> = PublishRelay.create()

    private val eventEmitter get() = eventRelay.hide()

    private val disposables: CompositeDisposable = CompositeDisposable()

    init {
        val (effectEmitter, viewStateEmitter) =
            asteroidViewFlow.start(eventEmitter)

        viewStateEmitter
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = { viewState -> viewStateLive.postValue(viewState) },
                onError = { error -> Log.e("qwer", "error viewstate", error) }
            ).addTo(disposables)

        effectEmitter
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = { effect -> viewEffectMutableLive.postValue(effect) },
                onError = { error -> Log.e("qwer", "error viewEffect", error) }
            ).addTo(disposables)
    }

    fun processEvent(viewEvent: AsteroidViewEvent) {
        eventRelay.accept(viewEvent)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}