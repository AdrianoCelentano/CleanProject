package com.clean.data.config

import android.app.Application
import com.clean.domain.asteroid.NasaRepository
import com.clean.domain.asteroid.StringProvider
import dagger.BindsInstance
import dagger.Component

@Component(modules = arrayOf(DataModule::class))
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