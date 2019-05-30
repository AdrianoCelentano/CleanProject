package com.clean.asteroids

import com.clean.domain.Asteroid

data class AsteroidViewState(val asteroid: Asteroid?,
                             val loading: Boolean,
                             val error: Throwable?)