package com.clean.data.config

import android.app.Application
import com.clean.domain.asteroid.NasaRepository
import com.clean.domain.asteroid.StringProvider
import dagger.BindsInstance
import dagger.Component
import javax.inject.Scope

@Component(modules = arrayOf(DataModule::class))
@DataSingleton
interface DataComponent {

    fun provideNasaRepository(): NasaRepository

    fun provideStringProvider(): StringProvider

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder
        fun build(): DataComponent
    }
}

@Scope
@MustBeDocumented
@Retention
annotation class DataSingleton