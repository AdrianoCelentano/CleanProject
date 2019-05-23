package com.clean.asteroids

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.clean.asteroids.model.Asteroid
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class AsteroidViewModel @Inject constructor(
    intentToResultProcessor: IntentToResultProcessor,
    viewStateReducer: AsteroidViewStateReducer
) : ViewModel() {

    val disposables: CompositeDisposable = CompositeDisposable()

    val intentSubject: PublishSubject<ViewIntent> = PublishSubject.create()

    private val viewStateMutableLive by lazy { MutableLiveData<AsteroidViewState>() }

    val viewStateLive
        get() = viewStateMutableLive

    private val viewEffectMutableLive by lazy { MutableLiveData<String>() }

    val viewEffectLive
        get() = viewEffectMutableLive

    init {
        intentToResult(intentToResultProcessor).publish { share ->
            emitEffect(share)
            share.ofType(Result.NewAsteroid::class.java)
        }.updateViewState(viewStateReducer)
    }

    private fun intentToResult(intentToResultProcessor: IntentToResultProcessor): Observable<Result> {
        return intentSubject
            .startWith(ViewIntent.Init)
            .doOnNext { Log.d("qwer", "intent: $it") }
            .flatMap(intentToResultProcessor::process)
            .doOnNext { Log.d("qwer", "result: $it") }
    }

    private fun emitEffect(share: Observable<Result>) {
        share.ofType(Result.Effect::class.java)
            .subscribeBy(
                onNext = {
                    viewEffectMutableLive.postValue("test")
                }
            ).addTo(disposables)
    }

    private fun Observable<Result.NewAsteroid>.updateViewState(viewStateReducer: AsteroidViewStateReducer) {
        compose(viewStateReducer.reduce())
            .doOnNext { Log.d("qwer", "viewstate: $it") }
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = { viewStateMutableLive.postValue(it) },
                onError = { Log.e("qwer", "error", it) }
            ).addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun processIntent(intent: ViewIntent) {
        intentSubject.onNext(intent)
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