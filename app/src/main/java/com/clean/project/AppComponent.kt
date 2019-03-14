package com.clean.project

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, ActivityComponentModule::class))
interface AppComponent {

    fun inject(mainActivity: MainActivity)

    @Component.Builder
    interface Builder {

        fun build(): AppComponent
        @BindsInstance
        fun application(application: Application): Builder
    }
}