package com.shpp.eorlov.assignment2

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.shpp.eorlov.assignment2.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule)
            Fresco.initialize(this@App)
        }
    }
}