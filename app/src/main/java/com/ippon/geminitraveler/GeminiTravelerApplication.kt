package com.ippon.geminitraveler

import android.app.Application
import com.ippon.geminitraveler.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class GeminiTravelerApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()
            androidContext(this@GeminiTravelerApplication)
            modules(appModule)
        }
    }
}