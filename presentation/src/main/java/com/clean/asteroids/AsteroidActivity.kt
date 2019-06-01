package com.clean.asteroids

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.clean.asteroids.config.CoreComponentProvider
import com.clean.asteroids.config.DaggerPresentationComponent
import com.clean.asteroids.config.PresentationComponent
import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewResult.AsteroidViewEffect
import com.clean.domain.asteroid.model.AsteroidViewState
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
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

    @Inject
    lateinit var asteroidViewRenderer: AsteroidViewRenderer

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
        observeEffects()
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
        Observable.merge<AsteroidViewEvent>(RefreshButtonObservable(), StoreButtonObservable())
            .observeOn(Schedulers.io())
            .subscribeBy(
                onNext = { asteroidViewModel.processEvent(it) }
            ).addTo(disposables)
    }

    private fun observeModel() {
        asteroidViewModel.viewStateLive.observe(this, object : LifecycleObserver<AsteroidViewState> {
            override fun onChanged(asteroidViewState: AsteroidViewState) {
                asteroidViewRenderer.render(asteroidViewState)
            }
        })
    }

    private fun observeEffects() {
        asteroidViewModel.viewEffectEmitter
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { effect ->
                    handleEffect(effect)
                }
            ).addTo(disposables)
    }

    private fun handleEffect(effect: AsteroidViewEffect?) {
        when (effect) {
            is AsteroidViewEffect.UserMessage -> showToast(effect)
        }
    }

    private fun showToast(effect: AsteroidViewEffect.UserMessage) {
        Toast.makeText(this, effect.message, Toast.LENGTH_SHORT).show()
    }

    private fun StoreButtonObservable(): Observable<AsteroidViewEvent> {
        return StoreButton.clicks()
            .map { AsteroidViewEvent.Store }
    }

    private fun RefreshButtonObservable(): Observable<AsteroidViewEvent> {
        return RefreshButton.clicks()
            .map { AsteroidViewEvent.Refresh }
    }



    private fun injectMembers() {
        component = DaggerPresentationComponent.builder()
            .activity(this)
            .coreComponent((application as CoreComponentProvider).provide())
            .build()
            .also { it.inject(this) }
    }
}
