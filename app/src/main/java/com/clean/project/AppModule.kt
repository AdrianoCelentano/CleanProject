package com.clean.project

import dagger.Module

@Module(includes = [AppBindsModule::class])
object AppModule {

}

@Module
interface AppBindsModule {

}
