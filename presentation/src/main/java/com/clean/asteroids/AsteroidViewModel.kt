package com.clean.asteroids

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.clean.asteroids.mapper.AsteroidMapper
import com.clean.asteroids.model.Asteroid
import com.clean.domain.GetAsteroidOfTheDay
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class AsteroidViewModel @Inject constructor(
    getAsteroidOfTheDay: GetAsteroidOfTheDay
) : ViewModel() {

    val disposables: CompositeDisposable = CompositeDisposable()

    val intentSubject: PublishSubject<ViewIntent> = PublishSubject.create()

    private val asteroidMutableLive by lazy { MutableLiveData<AsteroidViewState>() }

    val asteroidLive
        get() = asteroidMutableLive

    init {
        intentSubject
            .startWith(ViewIntent.Init)
            .flatMap { IntentToResultProcessor(getAsteroidOfTheDay).process(it) }
            .scan(AsteroidViewState(null, false, null))
            { oldviewstate: AsteroidViewState, result: Asteroid ->
                Log.d("qwer", "result: $result")
                AsteroidViewState(result, false, null)
            }
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = {
                    Log.d("qwer", "viewstate: $it")
                    asteroidMutableLive.postValue(it)
                },
                onError = {
                    Log.e("qwer", "error", it)
                }
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