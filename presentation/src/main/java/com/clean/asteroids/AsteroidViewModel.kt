package com.clean.asteroids

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.clean.domain.asteroid.AsteroidEventHandler
import com.clean.domain.asteroid.AsteroidViewStateReducer
import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewResult
import com.clean.domain.asteroid.model.AsteroidViewState
import com.clean.domain.util.rx.SimpleSubscriber
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AsteroidViewModel @Inject constructor(
    eventHandler: AsteroidEventHandler,
    viewStateReducer: AsteroidViewStateReducer
) : ViewModel() {

    val viewStateLive
        get() = viewStateMutableLive

    val viewEffectLive
        get() = viewEffectMutableLive

    private val viewStateMutableLive by lazy { MutableLiveData<AsteroidViewState>() }

    private val viewEffectMutableLive by lazy { MutableLiveData<AsteroidViewResult.AsteroidViewEffect>() }

    private val disposables: CompositeDisposable = CompositeDisposable()

    private val eventRelay: PublishRelay<AsteroidViewEvent> = PublishRelay.create()

    private val eventEmitter get() = eventRelay.hide()

    init {
        eventEmitter
            .handleEvent(eventHandler)
            .share()
            .also { sharedResultEmitter ->
                observeEffects(sharedResultEmitter)
                observeViewStateChange(sharedResultEmitter, viewStateReducer)
            }
    }

    fun processEvent(viewEvent: AsteroidViewEvent) {
        eventRelay.accept(viewEvent)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    private fun Observable<AsteroidViewEvent>.handleEvent(
        eventToResultProcessor: AsteroidEventHandler
    ): Observable<AsteroidViewResult> {
        return startWith(AsteroidViewEvent.Init)
            .doOnNext { Log.d("qwer", "intent: $it") }
            .flatMap(eventToResultProcessor::process)
            .doOnNext { Log.d("qwer", "result: $it") }
    }

    private fun observeEffects(share: Observable<AsteroidViewResult>) {
        share.ofType(AsteroidViewResult.AsteroidViewEffect::class.java)
            .subscribeOn(Schedulers.io())
            .subscribeWith(effectSubscriber)
            .addTo(disposables)
    }

    private val effectSubscriber
        get() = SimpleSubscriber<AsteroidViewResult.AsteroidViewEffect>(
            onNext = { effect -> viewEffectMutableLive.postValue(effect) },
            onError = { error -> Log.e("qwer", "error viewEffect", error) }
        )

    private fun observeViewStateChange(
        share: Observable<AsteroidViewResult>,
        viewStateReducer: AsteroidViewStateReducer
    ) {
        share.ofType(AsteroidViewResult.AsteroidPartialState::class.java)
            .compose(viewStateReducer.reduce())
            .doOnNext { Log.d("qwer", "viewstate: $it") }
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .subscribeWith(viewStateSubscriber)
            .addTo(disposables)
    }

    private val viewStateSubscriber
        get() = SimpleSubscriber<AsteroidViewState>(
            onNext = { viewState -> viewStateLive.postValue(viewState) },
            onError = { error -> Log.e("qwer", "error viewstate", error) }
        )
}