package com.clean.asteroids

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.clean.asteroids.config.ActivityScope
import com.clean.domain.asteroid.model.AsteroidViewState
import com.clean.domain.asteroid.model.ViewData
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

@ActivityScope
class AsteroidViewRenderer @Inject constructor(private val activity: Activity) {

    private val loadingIndicator: ProgressBar by lazy(LazyThreadSafetyMode.NONE)
    { activity.findViewById<ProgressBar>(R.id.loadingIndicator) }

    private val asteroidTitleTextView: TextView by lazy(LazyThreadSafetyMode.NONE)
    { activity.findViewById<TextView>(R.id.asteroidTitle) }

    private val asteroidImage: ImageView by lazy(LazyThreadSafetyMode.NONE)
    { activity.findViewById<ImageView>(R.id.asteroidImage) }

    private val rootLayout: ConstraintLayout by lazy(LazyThreadSafetyMode.NONE)
    { activity.findViewById<ConstraintLayout>(R.id.rootLayout) }

    private var errorSnackBar: Snackbar? = null

    fun render(asteroidViewState: AsteroidViewState) {
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
        asteroidTitleTextView.text = asteroid.title
        Glide.with(activity).load(asteroid.imageUrl).into(asteroidImage);
    }

    private fun showErrorMessage(errorMessage: String) {
        hideLoading()
        showSnackBar(errorMessage)
    }

    private fun showLoading() {
        loadingIndicator.visibility = View.VISIBLE
    }

    private fun hideErrorMessage() {
        errorSnackBar?.dismiss()
    }

    private fun hideLoading() {
        loadingIndicator.visibility = View.INVISIBLE
    }

    private fun showSnackBar(errorMessage: String) {
        errorSnackBar = Snackbar.make(rootLayout, errorMessage, Snackbar.LENGTH_INDEFINITE)
            .also { it.show() };
    }
}