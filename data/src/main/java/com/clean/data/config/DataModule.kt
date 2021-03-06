package com.clean.data.config

import android.app.Application
import androidx.room.Room
import com.clean.data.BuildConfig
import com.clean.data.NasaRepositoryImpl
import com.clean.data.StringProviderImpl
import com.clean.data.cache.DBConstants.DB_NAME
import com.clean.data.cache.NasaDataBase
import com.clean.data.remote.NasaService
import com.clean.domain.asteroid.NasaRepository
import com.clean.domain.asteroid.StringProvider
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module(includes = [DataBindsModule::class])
object DataModule {

    @Provides
    @JvmStatic
    fun provideIsDebug() = BuildConfig.DEBUG

    @Provides
    @JvmStatic
    @DataSingleton
    fun provideNasaDB(application: Application): NasaDataBase {
        return Room.databaseBuilder(
            application,
            NasaDataBase::class.java, DB_NAME
        ).build()
    }

    @Provides
    @JvmStatic
    fun provideNasaService(okHttpClient: OkHttpClient, gson: Gson): NasaService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/planetary/")
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(NasaService::class.java)
    }

    @Provides
    @JvmStatic
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @JvmStatic
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @JvmStatic
    fun provideLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = if (isDebug) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return logging
    }
}

@Module
interface DataBindsModule {

    @Binds
    fun bindNasaRepository(naseRepositoryImpl: NasaRepositoryImpl): NasaRepository

    @Binds
    fun bindStringProvider(stringProviderImpl: StringProviderImpl): StringProvider
}
