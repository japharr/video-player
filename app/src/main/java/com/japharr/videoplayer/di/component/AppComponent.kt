package com.japharr.videoplayer.di.component

import com.japharr.videoplayer.di.module.AppModule
import com.japharr.videoplayer.di.module.NetworkModule
import com.japharr.videoplayer.di.module.RoomModule
import com.japharr.videoplayer.ui.main.activity.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Japharr on 8/5/2018.
 */
@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, RoomModule::class])
interface AppComponent {
    fun inject(target: MainActivity)
}