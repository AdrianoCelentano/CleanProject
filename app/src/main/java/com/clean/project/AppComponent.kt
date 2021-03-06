package com.clean.project

import com.clean.asteroids.config.CoreComponent
import com.clean.data.config.DataComponent
import com.clean.domain.asteroid.AsteroidViewFlow
import dagger.Component
import javax.inject.Scope

@AppSingleton
@Component(
    modules = arrayOf(AppModule::class),
    dependencies = arrayOf(DataComponent::class)
)
interface AppComponent : CoreComponent {

    override fun provideAsteriodFlow(): AsteroidViewFlow

    @Component.Builder
    interface Builder {

        fun dataComponent(dataComponent: DataComponent): Builder
        fun build(): AppComponent
    }
}

@Scope
@MustBeDocumented
@Retention
annotation class AppSingleton