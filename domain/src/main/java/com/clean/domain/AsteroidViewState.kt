package com.clean.domain

import com.clean.domain.Asteroid

data class AsteroidViewState(val asteroid: Asteroid?,
                             val loading: Boolean,
                             val error: Throwable?)