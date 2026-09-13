package com.example.S8107253assingment2

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Assignment2App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Assignment2App)
            modules(appModule)
        }
    }
}