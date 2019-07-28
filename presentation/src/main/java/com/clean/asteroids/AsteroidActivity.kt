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
import com.clean.domain.asteroid.model.AsteroidViewResult
import com.clean.domain.asteroid.model.AsteroidViewState
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.Observer as LifecycleObserver

class AsteroidActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var asteroidViewRenderer: AsteroidViewRenderer

    @Inject
    lateinit var asteroidEffectHandler: AsteroidEffectHandler

    private val asteroidViewModel: AsteroidViewModel by viewModels(::viewModelFactory)

    private var component: PresentationComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        injectMembers()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        storeButtonListener()
        refreshButtonListener()
        observeModel()
        observeEffects()
    }

    override fun onDestroy() {
        super.onDestroy()
        component = null
        cancel()
    }

    private fun observeModel() {
        asteroidViewModel.viewStateLive.observe(this, object : LifecycleObserver<AsteroidViewState> {
            override fun onChanged(asteroidViewState: AsteroidViewState) {
                asteroidViewRenderer.render(asteroidViewState)
            }
        })
    }

    private fun observeEffects() {
        launch {
            for (effect in asteroidViewModel.effectChannel) {
                when (effect) {
                    is AsteroidViewResult.AsteroidViewEffect.UserMessage -> {
                        Toast.makeText(this@AsteroidActivity, effect.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun storeButtonListener() {
        StoreButton.setOnClickListener {
            asteroidViewModel.processEvent(AsteroidViewEvent.Store(asteroidViewModel.asteroidOfTheDay!!))
        }
    }

    private fun refreshButtonListener() {
        RefreshButton.setOnClickListener {
            asteroidViewModel.processEvent(AsteroidViewEvent.Load)
        }
    }

    private fun injectMembers() {
        component = DaggerPresentationComponent.builder()
            .activity(this)
            .coreComponent((application as CoreComponentProvider).provide())
            .build()
            .also { it.inject(this) }
    }
}
