package com.clean.project

import android.app.Application
import com.clean.asteroids.MainActivity
import com.clean.data.config.DataComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = arrayOf(AppModule::class, ActivityComponentModule::class),
    dependencies = arrayOf(DataComponent::class)
)
interface AppComponent {

    fun inject(mainActivity: MainActivity)

    @Component.Builder
    interface Builder {

        fun dataComponent(dataComponent: DataComponent): Builder
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}