package com.clean.asteroids

import android.app.Activity

interface ActivityInjector {

    fun inject(activity: Activity)
}