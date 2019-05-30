package com.clean.asteroids

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.clean.asteroids.config.CoreComponentProvider
import com.clean.asteroids.config.DaggerPresentationComponent
import com.clean.asteroids.config.PresentationComponent
import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewResult.AsteroidViewEffect
import com.clean.domain.asteroid.model.AsteroidViewState
import com.clean.domain.asteroid.model.ViewData
import com.google.android.material.snackbar.Snackbar
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

    private var snackBar: Snackbar? = null

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
        Observable.merge<AsteroidViewEvent>(RefreshButtonObservable(), StoreButtonObservable())
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
        asteroidViewModel.viewEffectLive.observe(this, object : LifecycleObserver<AsteroidViewEffect> {
            override fun onChanged(effect: AsteroidViewEffect) {
                when (effect) {
                    is AsteroidViewEffect.UserMessage -> showToast(effect)
                }
            }
        })
    }

    private fun StoreButtonObservable(): Observable<AsteroidViewEvent> {
        return StoreButton.clicks()
            .map { AsteroidViewEvent.Store }
    }

    private fun RefreshButtonObservable(): Observable<AsteroidViewEvent> {
        return RefreshButton.clicks()
            .map { AsteroidViewEvent.Refresh }
    }

    private fun render(asteroidViewState: AsteroidViewState) {
        when {
            asteroidViewState.loading == true -> showLoading()
            asteroidViewState.errorMessage.isNullOrBlank().not() -> showErrorMessage(asteroidViewState.errorMessage!!)
            asteroidViewState.data != null -> renderData(asteroidViewState.data!!)
        }
    }

    private fun renderData(viewData: ViewData) {
        hideLoading()
        hideErrorMessage()
        val asteroid = viewData.asteroid
        Text.text = asteroid.title
        Glide.with(this@AsteroidActivity).load(asteroid?.url).into(Image);
    }

    private fun showErrorMessage(errorMessage: String) {
        hideLoading()
        showSnackBar(errorMessage)
    }

    private fun showToast(effect: AsteroidViewEffect.UserMessage) {
        Toast.makeText(this@AsteroidActivity, effect.message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading() {
        LoadingIndicator.visibility = View.VISIBLE
    }

    private fun hideErrorMessage() {
        snackBar?.dismiss()
    }

    private fun hideLoading() {
        LoadingIndicator.visibility = View.INVISIBLE
    }

    private fun showSnackBar(errorMessage: String) {
        snackBar = Snackbar.make(RootLayout, errorMessage, Snackbar.LENGTH_INDEFINITE)
            .also { it.show() };
    }

    private fun injectMembers() {
        component = DaggerPresentationComponent.builder()
            .activity(this)
            .coreComponent((application as CoreComponentProvider).provide())
            .build()
            .also { it.inject(this) }
    }
}
