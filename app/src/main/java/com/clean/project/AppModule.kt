package com.clean.project

import com.clean.data.NasaRepositoryImpl
import com.clean.data.mapper.AsteriodMapper
import com.clean.data.mapper.EntityMapper
import com.clean.data.remote.NasaService
import com.clean.data.remote.NasaServiceFactory
import com.clean.data.remote.model.Asteroid
import com.clean.domain.NasaRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [AppBindsModule::class])
object AppModule {

    @Provides
    @JvmStatic
    fun provideNasaService(): NasaService {
        return NasaServiceFactory.makeGithubTrendingService(BuildConfig.DEBUG)
    }
}

@Module
interface AppBindsModule {

    @Binds
    fun bindNasaRepository(naseRepositoryImpl: NasaRepositoryImpl): NasaRepository

    @Binds
    fun bindNasaAsteroidMapper(asteriodMapper: AsteriodMapper):
            EntityMapper<Asteroid, com.clean.domain.Asteroid>
}
