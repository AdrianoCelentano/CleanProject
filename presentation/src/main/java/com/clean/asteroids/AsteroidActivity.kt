package com.clean.asteroids

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.clean.asteroids.config.CoreComponentProvider
import com.clean.asteroids.config.DaggerPresentationComponent
import com.clean.asteroids.config.PresentationComponent
import com.clean.domain.AsteroidViewState
import com.clean.domain.ViewEvent
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import androidx.lifecycle.Observer as LifecycleObserver

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
        observeEffects()
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
        Observable.merge<ViewEvent>(RefreshButtonObservable(), StoreButtonObservable())
            .observeOn(Schedulers.io())
            .subscribeBy(
                onNext = { asteroidViewModel.processEvent(it) }
            ).addTo(disposables)
    }

    private fun observeModel() {
        asteroidViewModel.viewStateLive.observe(this, object : LifecycleObserver<AsteroidViewState> {
            override fun onChanged(asteroidViewState: AsteroidViewState) {
                render(asteroidViewState)
            }
        })
    }

    private fun observeEffects() {
        asteroidViewModel.viewEffectLive.observe(this, object : LifecycleObserver<String> {
            override fun onChanged(text: String) {
                Toast.makeText(this@AsteroidActivity, text, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun StoreButtonObservable(): Observable<ViewEvent> {
        return StoreButton.clicks()
            .map { ViewEvent.Store }
    }

    private fun RefreshButtonObservable(): Observable<ViewEvent> {
        return RefreshButton.clicks()
            .map { ViewEvent.Refresh }
    }

    private fun render(asteroidViewState: AsteroidViewState) {
        val asteroid = asteroidViewState.asteroid
        Text.text = asteroid?.title
        Glide.with(this@AsteroidActivity).load(asteroid?.url).into(Image);
    }

    private fun injectMembers() {
        component = DaggerPresentationComponent.builder()
            .activity(this)
            .coreComponent((application as CoreComponentProvider).provide())
            .build()
            .also { it.inject(this) }
    }
}
