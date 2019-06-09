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
        renderLoading(asteroidViewState.loading)
        renderData(asteroidViewState.data)
        renderError(asteroidViewState.errorMessage)
    }

    private fun renderData(viewData: ViewData?) {
        if (viewData != null) {
            showData(viewData)
        } else {
            hideData()
        }
    }

    private fun renderError(errorMessage: String?) {
        if (errorMessage.isNullOrBlank()) {
            hideErrorMessage()
        } else {
            showErrorMessage(errorMessage)
        }
    }

    private fun renderLoading(loading: Boolean) {
        if (loading) {
            showLoading()
        } else {
            hideLoading()
        }
    }

    private fun hideData() {
        asteroidTitleTextView.visibility = View.INVISIBLE
        asteroidImage.visibility = View.INVISIBLE
    }

    private fun showData(viewData: ViewData) {
        val asteroid = viewData.asteroid
        asteroidTitleTextView.text = asteroid.title
        Glide.with(activity).load(asteroid.imageUrl).into(asteroidImage);
        asteroidTitleTextView.visibility = View.VISIBLE
        asteroidImage.visibility = View.VISIBLE
    }

    private fun showErrorMessage(errorMessage: String) {
        hideLoading()
        hideData()
        showSnackBar(errorMessage)
    }

    private fun hideErrorMessage() {
        errorSnackBar?.dismiss()
    }

    private fun showLoading() {
        loadingIndicator.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loadingIndicator.visibility = View.INVISIBLE
    }

    private fun showSnackBar(errorMessage: String) {
        errorSnackBar = Snackbar.make(rootLayout, errorMessage, Snackbar.LENGTH_INDEFINITE)
            .also { it.show() };
    }
}