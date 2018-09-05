package com.japharr.videoplayer.di.module

import android.content.Context
import com.japharr.videoplayer.App
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton

/**
 * Created by Japharr on 8/5/2018.
 */
@Module
class AppModule(val app: App) {
    @Provides
    @Singleton
    fun provideContext() : Context = app

    @Provides
    @Singleton
    fun provideApplication() : App = app

    @Provides
    @Singleton
    fun providesExecutor() : Executor = Executors.newFixedThreadPool(2)

}