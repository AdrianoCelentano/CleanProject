package com.clean.asteroids

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer as LifecycleObserver
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.clean.asteroids.config.CoreComponentProvider
import com.clean.asteroids.config.DaggerPresentationComponent
import com.clean.asteroids.config.PresentationComponent
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

import javax.inject.Inject

class AsteroidActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val asteroidViewModel: AsteroidViewModel by viewModels(::viewModelFactory)

    private var component: PresentationComponent? = null

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        injectMembers()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observeModel()
    }

    override fun onResume() {
        super.onResume()
        observeEvents()
    }

    override fun onPause() {
        super.onPause()
        disposables.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        component = null
    }

    private fun observeEvents() {
        Observable.merge<ViewIntent>(RefreshButtonObservable(), StoreButtonObservable())
            .observeOn(Schedulers.io())
            .subscribeBy(
                onNext = { asteroidViewModel.processIntent(it) }
            ).addTo(disposables)
    }

    private fun StoreButtonObservable(): Observable<ViewIntent> {
        return StoreButton.clicks()
            .map { ViewIntent.Store }
    }

    private fun RefreshButtonObservable(): Observable<ViewIntent> {
        return RefreshButton.clicks()
            .map { ViewIntent.Refresh }
    }

    private fun observeModel() {
        asteroidViewModel.asteroidLive.observe(this, object : LifecycleObserver<AsteroidViewState> {
            override fun onChanged(asteroidViewState: AsteroidViewState) {
                render(asteroidViewState)
            }
        })
    }

    private fun render(asteroidViewState: AsteroidViewState) {
        val asteroid = asteroidViewState.asteroid
        Text.text = asteroid?.name
        Glide.with(this@AsteroidActivity).load(asteroid?.imageUrl).into(Image);
    }

    private fun injectMembers() {
        component = DaggerPresentationComponent.builder()
            .activity(this)
            .coreComponent((application as CoreComponentProvider).provide())
            .build()
            .also { it.inject(this) }
    }
}
