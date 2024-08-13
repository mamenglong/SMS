package com.ml.base.app

import android.app.Application
import android.content.Context
import android.util.Log
import java.util.ServiceLoader

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceLoader.load(AppInit::class.java).forEach {
            Log.d("Application", "onCreate:AppInit:$it,${getProcessName()}")
            it.init(this)
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }
}
