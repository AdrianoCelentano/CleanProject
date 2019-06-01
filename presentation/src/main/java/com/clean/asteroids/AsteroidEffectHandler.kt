package com.clean.asteroids

import android.app.Activity
import android.widget.Toast
import com.clean.asteroids.config.ActivityScope
import com.clean.domain.asteroid.model.AsteroidViewResult
import javax.inject.Inject

@ActivityScope
class AsteroidEffectHandler @Inject constructor(private val activity: Activity) {

    fun handleEffect(effect: AsteroidViewResult.AsteroidViewEffect?) {
        when (effect) {
            is AsteroidViewResult.AsteroidViewEffect.UserMessage -> showToast(effect)
        }
    }

    private fun showToast(effect: AsteroidViewResult.AsteroidViewEffect.UserMessage) {
        Toast.makeText(activity, effect.message, Toast.LENGTH_SHORT).show()
    }


}