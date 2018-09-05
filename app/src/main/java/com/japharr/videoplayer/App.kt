package com.japharr.videoplayer

import android.app.Application
import com.japharr.videoplayer.di.component.AppComponent
import com.japharr.videoplayer.di.component.DaggerAppComponent
import com.japharr.videoplayer.di.module.AppModule
import com.japharr.videoplayer.di.module.RoomModule

/**
 * Created by Japharr on 8/5/2018.
 */
class App: Application() {
    val TAG = App::class.java.simpleName!!

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = initDagger(this)
    }

    private fun initDagger(app: App): AppComponent =
            DaggerAppComponent.builder()
                    .appModule(AppModule(app))
                    .roomModule(RoomModule(app))
                    .build()
}