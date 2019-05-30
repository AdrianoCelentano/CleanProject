package com.clean.data.config

import com.clean.domain.asteroid.NasaRepository
import dagger.Component

@Component(modules = arrayOf(DataModule::class))
interface DataComponent {

    fun provideNasaRepository(): NasaRepository

    @Component.Builder
    interface Builder {

        fun build(): DataComponent
    }
}