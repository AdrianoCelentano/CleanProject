package com.clean.asteroids

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.clean.domain.asteroid.AsteroidEventHandler
import com.clean.domain.asteroid.AsteroidViewStateReducer
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
            .subscribeBy(
                onNext = { viewEffectMutableLive.postValue(it) },
                onError = { Log.e("qwer", "error viewEffect", it) }
            ).addTo(disposables)
    }

    private fun observeViewStateChange(
        share: Observable<AsteroidViewResult>,
        viewStateReducer: AsteroidViewStateReducer
    ) {
        share.ofType(AsteroidViewResult.AsteroidPartialState::class.java)
            .compose(viewStateReducer.reduce())
            .doOnNext { Log.d("qwer", "viewstate: $it") }
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = { viewStateMutableLive.postValue(it) },
                onError = { Log.e("qwer", "error viewstate", it) }
            ).addTo(disposables)
    }
}