package com.ippon.geminitraveler

import android.app.Application
import com.ippon.geminitraveler.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.ksp.generated.module

class GeminiTravelerApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()
            androidContext(this@GeminiTravelerApplication)
            modules(AppModule().module)
        }
    }
}