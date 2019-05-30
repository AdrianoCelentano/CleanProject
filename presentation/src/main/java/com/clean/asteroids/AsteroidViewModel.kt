package com.clean.asteroids

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.clean.domain.Asteroid
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AsteroidViewModel @Inject constructor(
    eventToResultProcessor: EventToResultProcessor,
    viewStateReducer: AsteroidViewStateReducer
) : ViewModel() {

    val viewStateLive
        get() = viewStateMutableLive

    val viewEffectLive
        get() = viewEffectMutableLive

    private val viewStateMutableLive by lazy { MutableLiveData<AsteroidViewState>() }

    private val viewEffectMutableLive by lazy { MutableLiveData<String>() }

    private val disposables: CompositeDisposable = CompositeDisposable()

    private val eventRelay: PublishRelay<ViewEvent> = PublishRelay.create()

    private val eventObservable get() = eventRelay.hide()

    init {
        eventObservable
            .eventToResult(eventToResultProcessor)
            .share()
            .also { sharedResultObservable ->
                observeEffects(sharedResultObservable)
                observeViewStateChange(sharedResultObservable, viewStateReducer)
            }
    }

    fun processEvent(viewEvent: ViewEvent) {
        eventRelay.accept(viewEvent)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    private fun Observable<ViewEvent>.eventToResult(
        eventToResultProcessor: EventToResultProcessor
    ): Observable<Result> {
        return startWith(ViewEvent.Init)
            .doOnNext { Log.d("qwer", "intent: $it") }
            .concatMap(eventToResultProcessor::process)
            .doOnNext { Log.d("qwer", "result: $it") }
    }

    private fun observeEffects(share: Observable<Result>) {
        share.ofType(Result.Effect::class.java)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = { viewEffectMutableLive.postValue("test") },
                onError = { Log.e("qwer", "error effect", it) }
            ).addTo(disposables)
    }

    private fun observeViewStateChange(share: Observable<Result>, viewStateReducer: AsteroidViewStateReducer) {
        share.ofType(Result.NewAsteroid::class.java)
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

sealed class ViewEvent {
    object Init : ViewEvent()
    object Store : ViewEvent()
    object Refresh : ViewEvent()
}

sealed class Result {
    data class NewAsteroid(val asteroid: Asteroid) : Result()
    object Effect : Result()
}