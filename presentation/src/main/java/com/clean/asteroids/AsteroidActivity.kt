package com.clean.asteroids

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.clean.asteroids.config.CoreComponentProvider
import com.clean.asteroids.config.DaggerPresentationComponent
import com.clean.asteroids.config.PresentationComponent
import com.clean.asteroids.model.Asteroid
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class AsteroidActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val asteroidViewModel: AsteroidViewModel by viewModels(::viewModelFactory)

    private var component: PresentationComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        injectMembers()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observeAsteroidData()
    }

    private fun observeAsteroidData() {
        asteroidViewModel.asteroidLive.observe(this, object : Observer<Asteroid> {
            override fun onChanged(asteroid: Asteroid) {
                Text.text = asteroid.name
                Glide.with(this@AsteroidActivity).load(asteroid.imageUrl).into(Image);
            }
        })
    }

    private fun injectMembers() {
        component = DaggerPresentationComponent.builder()
            .activity(this)
            .coreComponent((application as CoreComponentProvider).provide())
            .build().also { it.inject(this) }
    }

    override fun onDestroy() {
        super.onDestroy()
        component = null
    }
}
