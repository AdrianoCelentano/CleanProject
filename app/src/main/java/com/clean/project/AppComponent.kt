package com.clean.project

import android.app.Application
import com.clean.asteroids.config.CoreComponent
import com.clean.data.config.DataComponent
import com.clean.domain.GetAsteroidOfTheDay
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = arrayOf(AppModule::class),
    dependencies = arrayOf(DataComponent::class)
)
interface AppComponent : CoreComponent {

    override fun provideGetAsteroidOfTheDay(): GetAsteroidOfTheDay

    @Component.Builder
    interface Builder {

        fun dataComponent(dataComponent: DataComponent): Builder
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}