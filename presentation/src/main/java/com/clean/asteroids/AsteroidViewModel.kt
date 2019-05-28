package com.clean.asteroids

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.clean.asteroids.model.Asteroid
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AsteroidViewModel @Inject constructor(
    intentToResultProcessor: IntentToResultProcessor,
    viewStateReducer: AsteroidViewStateReducer
) : ViewModel() {

    val viewStateLive
        get() = viewStateMutableLive

    val viewEffectLive
        get() = viewEffectMutableLive

    private val viewStateMutableLive by lazy { MutableLiveData<AsteroidViewState>() }

    private val viewEffectMutableLive by lazy { MutableLiveData<String>() }

    private val disposables: CompositeDisposable = CompositeDisposable()

    private val intentRelay: PublishRelay<ViewIntent> = PublishRelay.create()

    private val intentObservable get() = intentRelay.hide()

    init {
        intentObservable
            .intentToResult(intentToResultProcessor)
            .share()
            .also { sharedResultObservable ->
                observeEffects(sharedResultObservable)
                observeViewStateChange(sharedResultObservable, viewStateReducer)
            }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun processIntent(intent: ViewIntent) {
        intentRelay.accept(intent)
    }

    private fun Observable<ViewIntent>.intentToResult(
        intentToResultProcessor: IntentToResultProcessor
    ): Observable<Result> {
        return startWith(ViewIntent.Init)
            .doOnNext { Log.d("qwer", "intent: $it") }
            .concatMap(intentToResultProcessor::process)
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

sealed class ViewIntent {
    object Init : ViewIntent()
    object Store : ViewIntent()
    object Refresh : ViewIntent()
}

sealed class Result {
    data class NewAsteroid(val asteroid: Asteroid) : Result()
    object Effect : Result()
}